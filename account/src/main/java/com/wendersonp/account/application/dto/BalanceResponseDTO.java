package com.wendersonp.account.application.dto;

import com.wendersonp.account.core.model.BalanceModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BalanceResponseDTO(BigDecimal availableBalance, LocalDateTime lastUpdate) {
    public BalanceResponseDTO(BalanceModel balanceModel) {
        this(balanceModel.getBalance(), balanceModel.getLastUpdate());
    }
}
