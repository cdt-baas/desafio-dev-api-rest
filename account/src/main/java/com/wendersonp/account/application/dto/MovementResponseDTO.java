package com.wendersonp.account.application.dto;

import com.wendersonp.account.core.model.MovementModel;
import com.wendersonp.account.core.model.enumeration.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MovementResponseDTO(
    UUID identifier,
    UUID accountIdentifier,
    String accountNumber,
    String branch,
    BigDecimal balanceAfter,
    BigDecimal amount,
    MovementType type,
    LocalDateTime createdAt,
    String description
) {

    public MovementResponseDTO(MovementModel movementModel) {
        this(movementModel.getIdentifier(),
                movementModel.getAccount().getIdentifier(),
                movementModel.getAccount().getAccountNumber(),
                movementModel.getAccount().getBranch(),
                movementModel.getBalanceAfter(),
                movementModel.getAmount(),
                movementModel.getType(),
                movementModel.getCreatedAt(),
                movementModel.getDescription());
    }
}
