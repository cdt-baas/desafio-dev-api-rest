package com.wendersonp.account.core.model;

import com.wendersonp.account.core.model.enumeration.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovementModel {

    private UUID identifier;

    private AccountModel account;

    private BigDecimal amount;

    private BigDecimal balanceAfter;

    private LocalDateTime createdAt;

    private String description;

    private MovementType type;
}
