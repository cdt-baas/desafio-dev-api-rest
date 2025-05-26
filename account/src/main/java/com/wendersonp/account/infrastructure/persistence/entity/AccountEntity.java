package com.wendersonp.account.infrastructure.persistence.entity;

import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.enumeration.BlockStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_account")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID identifier;

    @Column(name = "fk_holder_id", unique = true)
    private UUID holderId;

    private String accountNumber;

    private String branch;

    @Column(precision = 10, scale = 2)
    private BigDecimal withdrawDailyLimit;

    private BlockStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_balance_id")
    private BalanceEntity balance;

    public AccountEntity(AccountModel accountModel, BalanceEntity balanceEntity) {
        this.identifier = accountModel.getIdentifier();
        this.holderId = accountModel.getHolderId();
        this.accountNumber = accountModel.getAccountNumber();
        this.branch = accountModel.getBranch();
        this.withdrawDailyLimit = accountModel.getWithdrawDailyLimit();
        this.status = accountModel.getStatus();
        this.createdAt = accountModel.getCreatedAt();
        this.balance = balanceEntity;
    }

    public AccountEntity(UUID identifier) {
        this.identifier = identifier;
    }

    public AccountModel toModel() {
        return AccountModel.builder()
                .identifier(identifier)
                .holderId(holderId)
                .accountNumber(accountNumber)
                .branch(branch)
                .withdrawDailyLimit(withdrawDailyLimit)
                .status(status)
                .createdAt(createdAt)
                .balance(() -> balance.toModel())
                .build();
    }
}
