package com.wendersonp.account.core.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceModel {

    private UUID identifier;

    private BigDecimal balance;

    private LocalDateTime lastUpdate;

}
