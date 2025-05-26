package com.wendersonp.account.core.service;

import com.wendersonp.account.core.exceptions.BusinessException;
import com.wendersonp.account.core.fixture.AccountFixture;
import com.wendersonp.account.core.fixture.BalanceFixture;
import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.BalanceModel;
import com.wendersonp.account.core.model.MovementModel;
import com.wendersonp.account.core.model.enumeration.MovementType;
import com.wendersonp.account.core.ports.driven.BalanceRepositoryDrivenPort;
import com.wendersonp.account.core.ports.driving.MovementCoreDrivingPort;
import com.wendersonp.account.util.ExceptionMessageEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceCoreServiceTest {

    @Mock
    private BalanceRepositoryDrivenPort balanceRepositoryDrivenPort;

    @Mock
    private MovementCoreDrivingPort movementCore;

    @InjectMocks
    private BalanceCoreService balanceCoreService;

    private static AccountModel accountModel;

    private static BalanceModel balanceModel;

    private static MovementType movementType;

    private static BigDecimal amount;

    private static String description;

    @BeforeEach
    void setUp() {
        accountModel = AccountFixture.getAccountModel();
        balanceModel = BalanceFixture.getBalanceModel();
        movementType = MovementType.DEPOSIT;
        amount = BigDecimal.valueOf(1000);
        description = "description";
        accountModel.setBalance(() -> balanceModel);
    }

    @Test
    void mustCreateBalanceSuccessfully() {
        when(balanceRepositoryDrivenPort.persist(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        var balanceResult = balanceCoreService.createBalance();

        assertNotNull(balanceResult);
        assertEquals(BigDecimal.ZERO, balanceResult.getBalance());
    }

    @Test
    void mustNotMakeMovementIfBalanceNotFound() {
        var balanceIdentifier = balanceModel.getIdentifier();
        when(balanceRepositoryDrivenPort.findByIdentifier(any())).thenReturn(null);

        Exception exception = assertThrows(BusinessException.class, () -> balanceCoreService.makeMovementOnBalance(accountModel, balanceIdentifier, movementType, amount, description));

        assertEquals(ExceptionMessageEnum.BALANCE_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void mustNotMakeWithdrawIfTotalWithdrawMadeIsAboveDailyLimit() {
        movementType = MovementType.WITHDRAW;
        var balanceIdentifier = balanceModel.getIdentifier();

        when(balanceRepositoryDrivenPort.findByIdentifier(any())).thenReturn(balanceModel);
        when(movementCore.getTotalWithdrawWithinADate(any(), any())).thenReturn(BigDecimal.valueOf(1950));

        Exception exception = assertThrows(BusinessException.class, () -> balanceCoreService.makeMovementOnBalance(accountModel, balanceIdentifier, movementType, amount, description));

        assertEquals(ExceptionMessageEnum.WITHDRAW_DAILY_LIMIT.getMessage(), exception.getMessage());
    }

    @Test
    void mustNotMakeWithdrawIfBalanceIsInsufficient() {
        movementType = MovementType.WITHDRAW;
        balanceModel.setBalance(BigDecimal.valueOf(100));
        var balanceIdentifier = balanceModel.getIdentifier();

        when(balanceRepositoryDrivenPort.findByIdentifier(any())).thenReturn(balanceModel);
        when(movementCore.getTotalWithdrawWithinADate(any(), any())).thenReturn(BigDecimal.ZERO);

        Exception exception = assertThrows(BusinessException.class, () -> balanceCoreService.makeMovementOnBalance(accountModel, balanceIdentifier, movementType, amount, description));

        assertEquals(ExceptionMessageEnum.INSUFFICIENT_BALANCE.getMessage(), exception.getMessage());
    }

    @Test
    void mustMakeWithdrawSuccessfully() {
        movementType = MovementType.WITHDRAW;
        balanceModel.setBalance(BigDecimal.valueOf(2000));
        amount = BigDecimal.valueOf(1000);

        var balanceIdentifier = balanceModel.getIdentifier();

        when(balanceRepositoryDrivenPort.findByIdentifier(any())).thenReturn(balanceModel);
        when(movementCore.getTotalWithdrawWithinADate(any(), any())).thenReturn(BigDecimal.ZERO);
        when(movementCore.registerMovement(any(), any(), any(), any(), any())).thenReturn(new MovementModel());
        when(balanceRepositoryDrivenPort.persist(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        var resultingBalance = balanceCoreService.makeMovementOnBalance(accountModel, balanceIdentifier, movementType, amount, description);

        assertNotNull(resultingBalance);
        assertEquals(BigDecimal.valueOf(1000), resultingBalance.getBalance());
    }

    @Test
    void mustMakeDepositSuccessfully() {
        movementType = MovementType.DEPOSIT;
        balanceModel.setBalance(BigDecimal.valueOf(2000));
        amount = BigDecimal.valueOf(1000);

        var balanceIdentifier = balanceModel.getIdentifier();

        when(balanceRepositoryDrivenPort.findByIdentifier(any())).thenReturn(balanceModel);
        when(movementCore.registerMovement(any(), any(), any(), any(), any())).thenReturn(new MovementModel());
        when(balanceRepositoryDrivenPort.persist(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        var resultingBalance = balanceCoreService.makeMovementOnBalance(accountModel, balanceIdentifier, movementType, amount, description);

        assertNotNull(resultingBalance);
        assertEquals(BigDecimal.valueOf(3000), resultingBalance.getBalance());
    }
}