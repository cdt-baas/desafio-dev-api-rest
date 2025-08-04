package br.com.desafio.digitalaccount.controllers;

import br.com.desafio.digitalaccount.dto.accountHolder.AccountHolderRequestDTO;
import br.com.desafio.digitalaccount.dto.accountHolder.AccountHolderResponseDTO;
import br.com.desafio.digitalaccount.services.AccountHolderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account-holders")
public class AccountHolderController {

    private final AccountHolderService accountHolderService;
    private static final Logger log = LoggerFactory.getLogger(AccountHolderController.class);

    public AccountHolderController(AccountHolderService accountHolderService) {
        this.accountHolderService = accountHolderService;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountHolderResponseDTO> createAccountHolder(@RequestBody AccountHolderRequestDTO dto) {
        log.info("Creating account holder: {}", dto.name());
        this.accountHolderService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AccountHolderResponseDTO("User created successfully!"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccountHolder(@RequestBody AccountHolderRequestDTO dto) {
        log.info("Deleting account holder: {}", dto.name());
        this.accountHolderService.delete(dto.cpf());
        return ResponseEntity.status(HttpStatus.OK).body(new AccountHolderResponseDTO("User deleted successfully!"));
    }
}
