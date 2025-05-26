package com.wendersonp.account.infrastructure.persistence.entity;

import com.wendersonp.account.core.model.MovementModel;
import com.wendersonp.account.core.model.enumeration.MovementType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_movement", indexes = {
    @Index(name = "idx_account_id", columnList = "fk_account_id, created_at")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID identifier;

    @ManyToOne
    @JoinColumn(name = "fk_account_id")
    private AccountEntity account;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(precision = 10, scale = 2)
    private BigDecimal balanceAfter;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(columnDefinition = "VARCHAR(255)")
    private String description;

    private MovementType type;

    public MovementEntity(MovementModel movementModel) {
        this.account = new AccountEntity(movementModel.getAccount().getIdentifier());
        this.amount = movementModel.getAmount();
        this.balanceAfter = movementModel.getBalanceAfter();
        this.createdAt = movementModel.getCreatedAt();
        this.description = movementModel.getDescription();
        this.type = movementModel.getType();
    }

    public MovementModel toModel() {
        return MovementModel.builder()
                .identifier(this.identifier)
                .account(this.account.toModel())
                .amount(this.amount)
                .balanceAfter(this.balanceAfter)
                .createdAt(this.createdAt)
                .description(this.description)
                .type(this.type)
                .build();
    }
}
