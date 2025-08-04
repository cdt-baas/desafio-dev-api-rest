package br.com.desafio.digitalaccount.services;

import br.com.desafio.digitalaccount.domain.accountHolder.AccountHolder;
import br.com.desafio.digitalaccount.domain.accountHolder.exceptions.AccountHolderAlreadyExistsException;
import br.com.desafio.digitalaccount.domain.accountHolder.exceptions.CPFNotFoundException;
import br.com.desafio.digitalaccount.dto.accountHolder.AccountHolderRequestDTO;
import br.com.desafio.digitalaccount.repositories.AccountHolderRepository;
import br.com.desafio.digitalaccount.utils.HashUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountHolderService {

    private final AccountHolderRepository accountHolderRepository;
    private final BytesEncryptor bytesEncryptor;
    private static final Logger log = LoggerFactory.getLogger(AccountHolderService.class);

    public AccountHolderService(AccountHolderRepository accountHolderRepository, BytesEncryptor bytesEncryptor) {
        this.accountHolderRepository = accountHolderRepository;
        this.bytesEncryptor = bytesEncryptor;
    }

    @Transactional
    public void save(AccountHolderRequestDTO dto) {
        try {
            String cpfHash = HashUtil.generateSha256(dto.cpf());
            if (this.accountHolderRepository.findByCpfHash(cpfHash).isPresent()) {
                log.error("Account holder already exists!");
                throw new AccountHolderAlreadyExistsException("Account holder already exists!");
            }
            byte[] encryptedCpf = this.bytesEncryptor.encrypt(dto.cpf().getBytes());
            AccountHolder accountHolder = new AccountHolder(dto.name(), encryptedCpf, cpfHash);
            log.info("Persisting account holder: id={}", accountHolder.getId());
            this.accountHolderRepository.save(accountHolder);
        } catch (Exception e) {
            log.error("Error persisting account holder: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void delete(String cpf) {
        try {
           String cpfSearchHash = HashUtil.generateSha256(cpf);
           Optional<AccountHolder> persistedAccountHolderOptional = this.accountHolderRepository.findByCpfHash(cpfSearchHash);
            if (!persistedAccountHolderOptional.isPresent()) {
                log.error("Cpf not found for account holder");
                throw new CPFNotFoundException("Cpf not found for account holder");
            }
            AccountHolder persistedAccountHolder = persistedAccountHolderOptional.get();
            String decryptedCpf = new String(this.bytesEncryptor.decrypt(persistedAccountHolder.getEncryptedCpf()));
            if (!decryptedCpf.equals(cpf)) {
                log.error("Informed CPF does not match persisted CPF");
                throw new CPFNotFoundException("CPF does not match");
            }
            log.info("Deleting account holder: {}", persistedAccountHolder.getName());
            this.accountHolderRepository.delete(persistedAccountHolder);
        } catch (Exception e) {
            log.error("Error deleting account holder {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
