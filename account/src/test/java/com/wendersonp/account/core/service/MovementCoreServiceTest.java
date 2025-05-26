package com.wendersonp.account.core.service;

import com.wendersonp.account.core.exceptions.BusinessException;
import com.wendersonp.account.core.fixture.AccountFixture;
import com.wendersonp.account.core.fixture.MovementFixture;
import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.MovementModel;
import com.wendersonp.account.core.model.enumeration.MovementType;
import com.wendersonp.account.core.ports.driven.MovementRepositoryDrivenPort;
import com.wendersonp.account.util.ExceptionMessageEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovementCoreServiceTest {

    @Mock
    private MovementRepositoryDrivenPort movementRepositoryDrivenPort;

    @InjectMocks
    private MovementCoreService movementCoreService;

    @Test
    void registerMovementWithSuccess() {
        AccountModel accountModel = AccountFixture.getAccountModel();
        BigDecimal amount = new BigDecimal(1000);
        BigDecimal balanceAfter = new BigDecimal(2300);
        String description = "Deposit";
        MovementType type = MovementType.DEPOSIT;

        when(movementRepositoryDrivenPort.persist(any(MovementModel.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        MovementModel movementModel = movementCoreService.registerMovement(accountModel, amount, balanceAfter, description, type);

        assertNotNull(movementModel);
        assertEquals(amount, movementModel.getAmount());
        assertEquals(balanceAfter, movementModel.getBalanceAfter());
        assertEquals(description, movementModel.getDescription());
        assertEquals(type, movementModel.getType());
    }

    @Test
    void getTotalWithdrawWithinADateWithSuccess() {
        LocalDate startDate = LocalDate.now();
        UUID accountIdentifier = UUID.randomUUID();
        BigDecimal total = new BigDecimal(1000);

        when(movementRepositoryDrivenPort.getTotalMovementWithinAPeriod(any(LocalDateTime.class), any(LocalDateTime.class), any(UUID.class), any(MovementType.class))).thenReturn(total);


        BigDecimal result = movementCoreService.getTotalWithdrawWithinADate(startDate, accountIdentifier);

        assertNotNull(result);
        assertEquals(total, result);
    }

    @Test
    void getMovementsWithinAPeriodWithSuccess() {
        LocalDate beginningDate = LocalDate.now().minusDays(7);
        LocalDate endingDate = LocalDate.now().minusDays(1);
        UUID accountIdentifier = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MovementModel> page = new PageImpl<>(List.of(
                MovementFixture.getMovementModel(AccountFixture.getAccountModel())
        ));

        when(movementRepositoryDrivenPort.getMovementsWithinAPeriod(any(LocalDateTime.class), any(LocalDateTime.class), any(UUID.class), any(Pageable.class))).thenReturn(page);

        Page<MovementModel> result = movementCoreService.getMovementsWithinAPeriod(beginningDate, endingDate, accountIdentifier, pageable);

        assertNotNull(result);
        assertArrayEquals(page.getContent().toArray(), result.getContent().toArray());
    }

    @Test
    void getMovementsWithinAPeriodInvalidDatesException() {
        LocalDate beginningDate = LocalDate.now().plusDays(1);
        LocalDate endingDate = LocalDate.now().minusDays(1);
        UUID accountIdentifier = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Exception exception = assertThrows(BusinessException.class, () -> movementCoreService.getMovementsWithinAPeriod(beginningDate, endingDate, accountIdentifier, pageable));

        assertEquals(ExceptionMessageEnum.INVALID_DATES.getMessage(), exception.getMessage());
    }

    @Test
    void getMovementsWithinAPeriodBeginningDateFutureException() {
        LocalDate beginningDate = LocalDate.now().plusDays(1);
        LocalDate endingDate = LocalDate.now().plusDays(3);
        UUID accountIdentifier = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Exception exception = assertThrows(BusinessException.class, () -> movementCoreService.getMovementsWithinAPeriod(beginningDate, endingDate, accountIdentifier, pageable));

        assertEquals(ExceptionMessageEnum.FUTURE_DATE.getMessage(), exception.getMessage());
    }

    @Test
    void getMovementsWithinAPeriodEndingDateFutureException() {
        LocalDate beginningDate = LocalDate.now().minusDays(1);
        LocalDate endingDate = LocalDate.now().plusDays(3);
        UUID accountIdentifier = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Exception exception = assertThrows(BusinessException.class, () -> movementCoreService.getMovementsWithinAPeriod(beginningDate, endingDate, accountIdentifier, pageable));

        assertEquals(ExceptionMessageEnum.FUTURE_DATE.getMessage(), exception.getMessage());
    }
}