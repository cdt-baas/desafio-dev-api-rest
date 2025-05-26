package com.wendersonp.account.core.fixture;

import com.wendersonp.account.core.model.BalanceModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class BalanceFixture {

    public static BalanceModel getBalanceModel() {
        return BalanceModel.builder()
                .identifier(UUID.randomUUID())
                .balance(BigDecimal.ZERO)
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    public static BalanceModel getBalanceModel(BigDecimal balance) {
        return BalanceModel.builder()
                .identifier(UUID.randomUUID())
                .balance(balance)
                .lastUpdate(LocalDateTime.now())
                .build();
    }
}
