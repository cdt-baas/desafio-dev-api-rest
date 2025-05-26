package com.wendersonp.account.core.model;

import com.wendersonp.account.core.model.enumeration.BlockStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountModel {

    private UUID identifier;

    private UUID holderId;

    private String accountNumber;

    private String branch;

    private BigDecimal withdrawDailyLimit;

    private BlockStatus status;

    private LocalDateTime createdAt;

    private Supplier<BalanceModel> balance;

}
