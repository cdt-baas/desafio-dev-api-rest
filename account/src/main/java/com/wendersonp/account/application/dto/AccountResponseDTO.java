package com.wendersonp.account.application.dto;

import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.enumeration.BlockStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponseDTO(
    UUID identifier,
    UUID holderId,
    String accountNumber,
    String branch,
    BigDecimal withdrawDailyLimit,
    BlockStatus status,
    LocalDateTime createdAt,
    BalanceResponseDTO balance
) {

    public AccountResponseDTO(AccountModel accountModel) {
        this(
            accountModel.getIdentifier(),
            accountModel.getHolderId(),
            accountModel.getAccountNumber(),
            accountModel.getBranch(),
            accountModel.getWithdrawDailyLimit(),
            accountModel.getStatus(),
            accountModel.getCreatedAt(),
            new BalanceResponseDTO(accountModel.getBalance().get())
        );
    }
}
