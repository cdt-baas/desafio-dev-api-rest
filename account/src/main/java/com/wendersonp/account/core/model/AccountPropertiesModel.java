package com.wendersonp.account.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountPropertiesModel {

    private String defaultBranch;
    private BigDecimal withdrawDefaultLimit;
}
