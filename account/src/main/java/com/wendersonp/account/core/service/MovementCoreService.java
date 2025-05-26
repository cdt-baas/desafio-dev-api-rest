package com.wendersonp.account.core.service;

import com.wendersonp.account.core.exceptions.BusinessException;
import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.MovementModel;
import com.wendersonp.account.core.model.enumeration.MovementType;
import com.wendersonp.account.core.ports.driven.MovementRepositoryDrivenPort;
import com.wendersonp.account.core.ports.driving.MovementCoreDrivingPort;
import com.wendersonp.account.util.ExceptionMessageEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovementCoreService implements MovementCoreDrivingPort {

    private final MovementRepositoryDrivenPort movementRepositoryDrivenPort;


    @Override
    public MovementModel registerMovement(AccountModel account, BigDecimal amount, BigDecimal balanceAfter, String description, MovementType type) {
        log.info("Registering movement for account {}", account.getIdentifier());
        MovementModel movementModel = MovementModel.builder()
                .account(account)
                .amount(amount)
                .balanceAfter(balanceAfter)
                .createdAt(LocalDateTime.now())
                .description(description)
                .type(type)
                .build();

        return movementRepositoryDrivenPort.persist(movementModel);
    }

    @Override
    public BigDecimal getTotalWithdrawWithinADate(LocalDate date, UUID accountIdentifier) {
        LocalDateTime startTime = date.atStartOfDay();
        LocalDateTime endTime = date.atTime(23, 59, 59);
        return movementRepositoryDrivenPort.getTotalMovementWithinAPeriod(startTime, endTime, accountIdentifier, MovementType.WITHDRAW);
    }

    @Override
    public Page<MovementModel> getMovementsWithinAPeriod(LocalDate beginningDate, LocalDate endingDate, UUID accountIdentifier, Pageable pageable) {
        validateDatesForMovement(beginningDate, endingDate);
        LocalDateTime startTime = beginningDate.atStartOfDay();
        LocalDateTime endTime = endingDate.atTime(23, 59, 59);
        return movementRepositoryDrivenPort.getMovementsWithinAPeriod(startTime, endTime, accountIdentifier, pageable);
    }

    private void validateDatesForMovement(LocalDate beginningDate, LocalDate endingDate) {
        if (endingDate.isBefore(beginningDate)) {
            throw new BusinessException(ExceptionMessageEnum.INVALID_DATES);
        }
        if (beginningDate.isAfter(LocalDate.now())) {
            throw new BusinessException(ExceptionMessageEnum.FUTURE_DATE);
        }
        if (endingDate.isAfter(LocalDate.now())) {
            throw new BusinessException(ExceptionMessageEnum.FUTURE_DATE);
        }
    }
}
