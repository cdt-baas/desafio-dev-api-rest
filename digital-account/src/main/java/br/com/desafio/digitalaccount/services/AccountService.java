package br.com.desafio.digitalaccount.services;

import br.com.desafio.digitalaccount.domain.account.Account;
import br.com.desafio.digitalaccount.domain.account.AccountStatus;
import br.com.desafio.digitalaccount.domain.account.exceptions.*;
import br.com.desafio.digitalaccount.domain.accountHolder.AccountHolder;
import br.com.desafio.digitalaccount.domain.accountHolder.exceptions.AccountHolderNotFoundException;
import br.com.desafio.digitalaccount.domain.accountHolder.exceptions.CPFNotFoundException;
import br.com.desafio.digitalaccount.domain.accountHolder.exceptions.CPFValidationException;
import br.com.desafio.digitalaccount.domain.accountHolder.exceptions.InvalidAccountHolderException;
import br.com.desafio.digitalaccount.domain.operation.Operation;
import br.com.desafio.digitalaccount.domain.operation.OperationType;
import br.com.desafio.digitalaccount.dto.account.*;
import br.com.desafio.digitalaccount.dto.operation.OperationResponseDTO;
import br.com.desafio.digitalaccount.repositories.AccountHolderRepository;
import br.com.desafio.digitalaccount.repositories.AccountRepository;
import br.com.desafio.digitalaccount.repositories.OperationRepository;
import br.com.desafio.digitalaccount.utils.AccountNumberGenerator;
import br.com.desafio.digitalaccount.utils.HashUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountHolderRepository accountHolderRepository;
    private final BytesEncryptor bytesEncryptor;
    private final OperationRepository operationRepository;
    private static final BigDecimal DAILY_WITHDRAWAL_LIMIT = new BigDecimal("2000.00");
    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    public AccountService(AccountRepository accountRepository, AccountHolderRepository accountHolderRepository, BytesEncryptor bytesEncryptor, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.accountHolderRepository = accountHolderRepository;
        this.bytesEncryptor = bytesEncryptor;
        this.operationRepository = operationRepository;
    }

    @Transactional
    public Account createAccount(AccountRequestDTO dto) {
        log.debug("Generating cpf hash");
        String cpfHash = HashUtil.generateSha256(dto.cpf());
        AccountHolder accountHolder = this.accountHolderRepository.findByCpfHash(cpfHash)
                .orElseThrow(() -> new AccountHolderNotFoundException("Account holder not found"));

        String decryptedCpf = new String(bytesEncryptor.decrypt(accountHolder.getEncryptedCpf()));
        if (!decryptedCpf.equals(dto.cpf())) {
            log.error("Informed CPF does not match persisted CPF");
            throw new CPFValidationException("Error validating CPF!");
        }

        Account account = new Account();
        account.setAccountHolder(accountHolder);
        account.setBalance(dto.initialBalance() != null ? dto.initialBalance() : BigDecimal.ZERO);
        account.setNumber(AccountNumberGenerator.generate());
        account.setAgency(AccountNumberGenerator.getDefaultAgency());
        account.setStatus(AccountStatus.ACTIVE);

        if (accountRepository.findByNumber(account.getNumber()).isPresent()) {
            log.error("Account already exists");
            throw new AccountAlreadyExistsException("Account already exists!");
        }
        log.info("Persisting account, number: {}", account.getNumber());
        return accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(AccountDeletionDTO dto) {
        Account accountToBeDeleted = this.accountRepository.findByNumber(dto.number())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        String cpfHash = HashUtil.generateSha256(dto.cpf());
        AccountHolder providedAccountHolder = this.accountHolderRepository.findByCpfHash(cpfHash)
                .orElseThrow(() -> new AccountHolderNotFoundException("Account holder not found"));

        String decryptedProvidedCpf = new String(bytesEncryptor.decrypt(providedAccountHolder.getEncryptedCpf()));
        if (!decryptedProvidedCpf.equals(dto.cpf())) {
            throw new CPFValidationException("Error validating CPF!");
        }

        if (!accountToBeDeleted.getAccountHolder().getId().equals(providedAccountHolder.getId())) {
            throw new InvalidAccountHolderException("The account " + accountToBeDeleted.getNumber() + " doesn't belong to the provided CPF.");
        }

        this.accountRepository.delete(accountToBeDeleted);
    }

    @Transactional
    public Account blockAccount(UpdateAccountStatusRequestDTO dto) {
        return updateAccountStatus(dto.accountNumber(), dto.accountHolderCpf(), AccountStatus.BLOCKED);
    }

    @Transactional
    public Account unblockAccount(UpdateAccountStatusRequestDTO dto) {
        return updateAccountStatus(dto.accountNumber(), dto.accountHolderCpf(), AccountStatus.ACTIVE);
    }

    @Transactional
    public Account deposit(AccountDepositRequestDTO dto) {
        Account account = this.findAndValidateAccount(dto.accountNumber(), dto.accountHolderCpf());

        if (account.getStatus() != AccountStatus.ACTIVE) {
            log.error("Unable to deposit, account number {} is blocked", dto.accountNumber());
            throw new BlockedAccountException("Unable to deposit, the account is blocked!");
        }

        account.setBalance(account.getBalance().add(dto.amount()));
        log.info("Updating balance, account number: {}", account.getNumber());
        Account updatedAccount = this.accountRepository.save(account);

        Operation operation = new Operation(dto.amount(), OperationType.DEPOSIT, updatedAccount);
        this.operationRepository.save(operation);

        return updatedAccount;
    }

    @Transactional
    public Account withdraw(AccountWithdrawalRequestDTO dto) {
        Account account = findAndValidateAccount(dto.accountNumber(), dto.accountHolderCpf());

        if (account.getStatus() != AccountStatus.ACTIVE) {
            log.error("Unable to withdraw, account number {} is blocked", dto.accountNumber());
            throw new BlockedAccountException("Unable to withdraw, the account is blocked!");
        }

        // Validação: Saldo suficiente
        if (account.getBalance().compareTo(dto.amount()) < 0) {
            log.error("Unable to withdraw, account number {} has not enough funds!", dto.accountNumber());
            throw new InsufficientFundsException("This account has not enough funds!");
        }

        //Daily Limit.
        BigDecimal todayWithdrawnAmount = operationRepository.findDailyWithdrawalSumByAccountNumber(
                        account.getNumber(), OperationType.WITHDRAWAL.name(), LocalDate.now())
                .orElse(BigDecimal.ZERO);

        if (todayWithdrawnAmount.add(dto.amount()).compareTo(DAILY_WITHDRAWAL_LIMIT) > 0) {
            log.error("Daily limit of R$ {} reached.", DAILY_WITHDRAWAL_LIMIT);
            throw new DailyLimitAlreadyReachedException("Daily limit of R$ " + DAILY_WITHDRAWAL_LIMIT + " reached. You have already withdrawn " + todayWithdrawnAmount + " today.");
        }

        // Realiza o saque
        account.setBalance(account.getBalance().subtract(dto.amount()));
        Account updatedAccount = this.accountRepository.save(account);

        // Registra a operação
        Operation operation = new Operation(dto.amount(), OperationType.WITHDRAWAL, updatedAccount);
        this.operationRepository.save(operation);

        return updatedAccount;
    }

    public List<OperationResponseDTO> getAccountStatement(String accountNumber, String accountHolderCpf, LocalDate startDate, LocalDate endDate) {
        Account account = findAndValidateAccount(accountNumber, accountHolderCpf);

        //Start date and end date in the same day
        LocalDateTime startDateTime = startDate.atStartOfDay(); // Início do dia
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // Fim do dia (23:59:59.999...)

        List<Operation> operations = operationRepository.findByAccountNumberAndDateTimeBetween(
                account.getNumber(), startDateTime, endDateTime
        );
        return operations.stream()
                .map(o -> new OperationResponseDTO(o.getAmount(), o.getDateTime(), o.getOperationType(), o.getAccount().getNumber()))
                .collect(Collectors.toList());
    }

    private Account updateAccountStatus(String accountNumber, String accountHolderCpf, AccountStatus newStatus) {
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        String cpfHash = HashUtil.generateSha256(accountHolderCpf);
        AccountHolder providedAccountHolder = accountHolderRepository.findByCpfHash(cpfHash)
                .orElseThrow(() -> new CPFNotFoundException("Cpf not found"));

        String decryptedProvidedCpf = new String(bytesEncryptor.decrypt(providedAccountHolder.getEncryptedCpf()));
        if (!decryptedProvidedCpf.equals(accountHolderCpf)) {
            throw new CPFValidationException("Could not validate Account holder's CPF!");
        }

        //Account belongs to account holder?
        if (!account.getAccountHolder().getId().equals(providedAccountHolder.getId())) {
            throw new InvalidAccountHolderException("The account " + accountNumber + " doesn't belong to the provided CPF.");
        }

        //Same status?
        if (account.getStatus() == newStatus) {
            throw new AccountStatusException("Account " + accountNumber + " already " + newStatus.name() + ".");
        }

        account.setStatus(newStatus);
        return this.accountRepository.save(account);
    }

    private Account findAndValidateAccount(String accountNumber, String accountHolderCpf) {
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account with number " + accountNumber + " not found."));

        String cpfHash = HashUtil.generateSha256(accountHolderCpf);
        AccountHolder providedAccountHolder = accountHolderRepository.findByCpfHash(cpfHash)
                .orElseThrow(() -> new AccountHolderNotFoundException("Account holder not found."));

        String decryptedProvidedCpf = new String(bytesEncryptor.decrypt(providedAccountHolder.getEncryptedCpf()));
        if (!decryptedProvidedCpf.equals(accountHolderCpf)) {
            throw new CPFValidationException("Could not validate Account holder's CPF!");
        }

        if (!account.getAccountHolder().getId().equals(providedAccountHolder.getId())) {
            throw new InvalidAccountHolderException("The account " + accountNumber + " doesn't belong to the provided CPF.");
        }
        return account;
    }
}
