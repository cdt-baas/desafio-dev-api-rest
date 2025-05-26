package com.wendersonp.account.core.fixture;

import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.MovementModel;
import com.wendersonp.account.core.model.enumeration.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovementFixture {

    public static MovementModel getMovementModel(AccountModel accountModel) {
        return MovementModel.builder()
                .createdAt(LocalDateTime.now())
                .type(MovementType.DEPOSIT)
                .account(accountModel)
                .amount(new BigDecimal(1000))
                .balanceAfter(new BigDecimal(2300))
                .description("Deposit")
                .build();
    }
}
