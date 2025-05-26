package com.wendersonp.account.core.ports.driven;

import com.wendersonp.account.core.model.BalanceModel;

import java.util.UUID;

public interface BalanceRepositoryDrivenPort {

    BalanceModel persist(BalanceModel balanceModel);

    BalanceModel findByIdentifier(UUID identifier);

    void delete(UUID balanceIdentifier);
}
