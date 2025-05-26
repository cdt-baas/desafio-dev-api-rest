package com.wendersonp.account.application.service;

import com.wendersonp.account.application.dto.AccountResponseDTO;
import com.wendersonp.account.application.dto.BalanceResponseDTO;
import com.wendersonp.account.application.dto.MovementRequestDTO;

import java.util.List;
import java.util.UUID;

public interface AccountApplicationService {

    AccountResponseDTO createAccount(String documentNumber);

    AccountResponseDTO findByIdentifier(UUID identifier);

    BalanceResponseDTO makeMovementOnBalance(MovementRequestDTO movementRequestDTO);

    AccountResponseDTO blockAccount(UUID identifier);

    AccountResponseDTO unblockAccount(UUID identifier);

    List<AccountResponseDTO> findAll();

    void closeAccount(UUID identifier);
}
