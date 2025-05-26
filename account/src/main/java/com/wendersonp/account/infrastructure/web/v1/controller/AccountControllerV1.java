package com.wendersonp.account.infrastructure.web.v1.controller;


import com.wendersonp.account.application.dto.AccountResponseDTO;
import com.wendersonp.account.application.dto.BalanceResponseDTO;
import com.wendersonp.account.application.dto.MovementRequestDTO;
import com.wendersonp.account.application.service.AccountApplicationService;
import com.wendersonp.account.infrastructure.web.v1.controller.docs.AccountControllerV1ApiDocs;
import com.wendersonp.account.infrastructure.web.v1.routes.V1Routes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(V1Routes.ACCOUNT_PATH)
@RequiredArgsConstructor
public class AccountControllerV1 implements AccountControllerV1ApiDocs {

    private final AccountApplicationService accountApplicationService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public AccountResponseDTO createAccount(@RequestParam @Valid @CPF String documentNumber) {
        return accountApplicationService.createAccount(documentNumber);
    }

    @GetMapping("/{identifier}")
    @ResponseStatus(value = HttpStatus.OK)
    public AccountResponseDTO findByIdentifier(@PathVariable UUID identifier) {
        return accountApplicationService.findByIdentifier(identifier);
    }

    @PostMapping(V1Routes.ACCOUNT_MOVEMENT_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public BalanceResponseDTO makeMovementOnBalance(@Valid @RequestBody MovementRequestDTO movementRequestDTO) {
        return accountApplicationService.makeMovementOnBalance(movementRequestDTO);
    }

    @PatchMapping(V1Routes.ACCOUNT_BLOCK_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public AccountResponseDTO blockAccount(@PathVariable UUID identifier) {
        return accountApplicationService.blockAccount(identifier);
    }

    @PatchMapping(V1Routes.ACCOUNT_UNBLOCK_PATH)
    @ResponseStatus(value = HttpStatus.OK)
    public AccountResponseDTO unblockAccount(@PathVariable UUID identifier) {
        return accountApplicationService.unblockAccount(identifier);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<AccountResponseDTO> findAll() {
        return accountApplicationService.findAll();
    }

    @DeleteMapping("/close/{identifier}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void closeAccount(@PathVariable UUID identifier) {
        accountApplicationService.closeAccount(identifier);
    }
}
