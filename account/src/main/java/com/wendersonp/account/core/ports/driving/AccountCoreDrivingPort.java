package com.wendersonp.account.core.ports.driving;

import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.model.BalanceModel;
import com.wendersonp.account.core.model.enumeration.BlockStatus;
import com.wendersonp.account.core.model.enumeration.MovementType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountCoreDrivingPort {

    AccountModel createAccount(String documentNumber);

    BalanceModel makeMovementOnBalance(
            UUID accountIdentifier,
            BigDecimal amount,
            MovementType type,
            String description
    );

    AccountModel findByIdentifier(UUID identifier);

    AccountModel setBlockStatus(UUID identifier, BlockStatus status);

    List<AccountModel> findAll();

    void closeAccount(UUID identifier);
}
