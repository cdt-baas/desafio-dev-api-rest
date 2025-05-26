package com.wendersonp.account.infrastructure.persistence.entity;

import com.wendersonp.account.core.model.BalanceModel;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_balance")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID identifier;

    @Column(precision = 10, scale = 2)
    private BigDecimal balance;

    @LastModifiedDate
    private LocalDateTime lastUpdate;


    public BalanceModel toModel() {
        return BalanceModel.builder()
                .identifier(identifier)
                .balance(balance)
                .lastUpdate(lastUpdate)
                .build();
    }

    public BalanceEntity(BalanceModel balanceModel) {
        this.identifier = balanceModel.getIdentifier();
        this.balance = balanceModel.getBalance();
        this.lastUpdate = balanceModel.getLastUpdate();
    }
}