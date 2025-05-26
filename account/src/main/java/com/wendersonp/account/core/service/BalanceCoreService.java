package com.wendersonp.account.core.service;

import com.wendersonp.account.core.exceptions.BusinessException;
import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.BalanceModel;
import com.wendersonp.account.core.model.enumeration.MovementType;
import com.wendersonp.account.core.ports.driven.BalanceRepositoryDrivenPort;
import com.wendersonp.account.core.ports.driving.BalanceCoreDrivingPort;
import com.wendersonp.account.core.ports.driving.MovementCoreDrivingPort;
import com.wendersonp.account.util.ExceptionMessageEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceCoreService implements BalanceCoreDrivingPort {

    private final BalanceRepositoryDrivenPort balanceRepositoryDrivenPort;

    private final MovementCoreDrivingPort movementCore;
    @Override
    public BalanceModel createBalance() {
        var balanceModel = BalanceModel.builder()
                .balance(BigDecimal.ZERO)
                .build();
        return balanceRepositoryDrivenPort.persist(balanceModel);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BalanceModel makeMovementOnBalance(
            AccountModel accountModel,
            UUID balanceIdentifier,
            MovementType type,
            BigDecimal amount,
            String description
    ) {
        BalanceModel balanceModel = balanceRepositoryDrivenPort.findByIdentifier(balanceIdentifier);
        if (balanceModel == null) {
            throw new BusinessException(ExceptionMessageEnum.BALANCE_NOT_FOUND);
        }

        if (type == MovementType.WITHDRAW) {
            validateBalanceForWithdraw(accountModel, balanceModel, amount);
        }

        log.info("Making movement on balance of account {}", accountModel.getIdentifier());

        balanceModel = applyMovementOnBalance(balanceModel, type, amount);

        movementCore.registerMovement(accountModel, amount, balanceModel.getBalance(), description, type);

        return balanceModel;
    }

    private void validateBalanceForWithdraw(AccountModel accountModel, BalanceModel balanceModel, BigDecimal amount) {
        BigDecimal totalWithdrawMade = movementCore.getTotalWithdrawWithinADate(LocalDate.now(), accountModel.getIdentifier());
        BigDecimal totalWithdrawToMake = totalWithdrawMade.add(amount);

        if (accountModel.getWithdrawDailyLimit().compareTo(totalWithdrawToMake) < 0) {
            throw new BusinessException(ExceptionMessageEnum.WITHDRAW_DAILY_LIMIT);
        }

        BigDecimal balance = balanceModel.getBalance();

        if (balance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ExceptionMessageEnum.INSUFFICIENT_BALANCE);
        }
    }

    private BalanceModel applyMovementOnBalance(BalanceModel balanceModel, MovementType type, BigDecimal amount) {
        if (type == MovementType.DEPOSIT) {
            balanceModel.setBalance(balanceModel.getBalance().add(amount));
        } else {
            balanceModel.setBalance(balanceModel.getBalance().subtract(amount));
        }

        return balanceRepositoryDrivenPort.persist(balanceModel);
    }
}
