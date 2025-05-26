package com.wendersonp.account.core.ports.driving;

import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.BalanceModel;
import com.wendersonp.account.core.model.enumeration.MovementType;

import java.math.BigDecimal;
import java.util.UUID;

public interface BalanceCoreDrivingPort {

    BalanceModel createBalance();

    BalanceModel makeMovementOnBalance(
            AccountModel accountModel,
            UUID balanceIdentifier,
            MovementType type,
            BigDecimal amount,
            String description
    );
}
