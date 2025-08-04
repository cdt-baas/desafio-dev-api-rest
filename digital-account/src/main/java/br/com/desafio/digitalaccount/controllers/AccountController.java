package br.com.desafio.digitalaccount.controllers;

import br.com.desafio.digitalaccount.domain.account.Account;
import br.com.desafio.digitalaccount.dto.StatementRequestDTO;
import br.com.desafio.digitalaccount.dto.account.*;
import br.com.desafio.digitalaccount.dto.operation.OperationResponseDTO;
import br.com.desafio.digitalaccount.services.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountRequestDTO dto) {
        log.info("Creating account {}", dto);
        Account account = accountService.createAccount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AccountResponseDTO(
                account.getBalance(), account.getNumber(), account.getAgency(), account.getStatus(), account.getAccountHolder().getName()
        ));
    }

    @DeleteMapping
    public ResponseEntity<AccountResponseDTO> deleteAccount(@RequestBody AccountDeletionDTO dto) {
        log.info("Deleting account {}", dto);
        this.accountService.deleteAccount(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/block")
    public ResponseEntity<AccountResponseDTO> blockAccount(@Valid @RequestBody UpdateAccountStatusRequestDTO dto) {
        log.info("Blocking account number {}", dto.accountNumber());
        Account account = this.accountService.blockAccount(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new AccountResponseDTO(account.getBalance(), account.getNumber(), account.getAgency(), account.getStatus(), account.getAccountHolder().getName()));
    }

    @PutMapping("/unblock")
    public ResponseEntity<AccountResponseDTO> unblockAccount(@Valid @RequestBody UpdateAccountStatusRequestDTO dto) {
        log.info("Unblocking account number {}", dto.accountNumber());
        Account account = this.accountService.unblockAccount(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new AccountResponseDTO(account.getBalance(), account.getNumber(), account.getAgency(), account.getStatus(), account.getAccountHolder().getName()));
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountResponseDTO> deposit(@Valid @RequestBody AccountDepositRequestDTO dto) {
        log.info("Depositing R$ {} into account number {}", dto.amount(), dto.accountNumber());
        Account account = this.accountService.deposit(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new AccountResponseDTO(account.getBalance(), account.getNumber(), account.getAgency(), account.getStatus(), account.getAccountHolder().getName()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountResponseDTO> withdrawal(@Valid @RequestBody AccountWithdrawalRequestDTO dto) {
        log.info("Withdrawing R$ {} from account number {}", dto.amount(), dto.accountNumber());
        Account account = this.accountService.withdraw(dto);
        return ResponseEntity.status(HttpStatus.OK).body(new AccountResponseDTO(account.getBalance(), account.getNumber(), account.getAgency(), account.getStatus(), account.getAccountHolder().getName()));
    }

    @GetMapping("/statement")
    public ResponseEntity<?> getStatement(@Valid @RequestBody StatementRequestDTO dto) {
        log.info("Retrieving statement for account number {}", dto.accountNumber());
        List<OperationResponseDTO> statement = accountService.getAccountStatement(
                dto.accountNumber(), dto.accountHolderCpf(), dto.startDate(), dto.endDate()
        );
        return ResponseEntity.status(HttpStatus.OK).body(statement);
    }

}
