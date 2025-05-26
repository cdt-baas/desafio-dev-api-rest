package com.wendersonp.account.core.fixture;

import com.wendersonp.account.core.model.AccountPropertiesModel;

import java.math.BigDecimal;

public class AccountPropertiesFixture {

    public static AccountPropertiesModel getAccountProperties() {
        return AccountPropertiesModel.builder()
                .defaultBranch("1")
                .withdrawDefaultLimit(new BigDecimal(2000))
                .build();
    }
}
