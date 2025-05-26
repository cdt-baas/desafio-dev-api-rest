package com.wendersonp.account.core.ports.driven;

import com.wendersonp.account.core.model.AccountModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryDrivenPort {

    AccountModel persist(AccountModel accountModel);

    boolean exists(UUID holderId);

    Optional<AccountModel> findByIdentifier(UUID identifier);

    List<AccountModel> findAll();


    Optional<AccountModel> findByHolderId(UUID holderId);
}
