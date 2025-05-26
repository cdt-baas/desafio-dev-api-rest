package com.wendersonp.account.infrastructure.adapters;

import com.wendersonp.account.core.model.AccountPropertiesModel;
import com.wendersonp.account.core.ports.driven.AccountDefaultPropertiesDrivenPort;
import com.wendersonp.account.infrastructure.config.AccountDefaultProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@ConfigurationProperties(prefix = "account")
@RequiredArgsConstructor
public class AccountDefaultPropertiesServiceAdapter implements AccountDefaultPropertiesDrivenPort {

    private final AccountDefaultProperties accountDefaultProperties;

    @Override
    public AccountPropertiesModel getDefaultProperties() {
        return AccountPropertiesModel.builder()
                .defaultBranch(accountDefaultProperties.getDefaultBranch())
                .withdrawDefaultLimit(new BigDecimal(accountDefaultProperties.getWithdrawDefaultLimit()).setScale(2, RoundingMode.HALF_UP))
                .build();
    }
}
