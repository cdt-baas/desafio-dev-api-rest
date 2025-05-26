package com.wendersonp.account.core.fixture;


import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.enumeration.BlockStatus;
import com.wendersonp.account.util.GenerationUtils;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountFixture {

    public static AccountModel getAccountModel() {
        return AccountModel.builder()
                .identifier(UUID.randomUUID())
                .holderId(UUID.randomUUID())
                .accountNumber(GenerationUtils.generateAccountNumber())
                .branch("1")
                .withdrawDailyLimit(new BigDecimal(2000))
                .status(BlockStatus.UNBLOCKED)
                .build();
    }

}
