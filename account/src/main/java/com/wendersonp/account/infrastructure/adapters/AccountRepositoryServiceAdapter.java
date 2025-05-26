package com.wendersonp.account.infrastructure.adapters;

import com.wendersonp.account.core.model.AccountModel;
import com.wendersonp.account.core.ports.driven.AccountRepositoryDrivenPort;
import com.wendersonp.account.infrastructure.persistence.entity.AccountEntity;
import com.wendersonp.account.infrastructure.persistence.entity.BalanceEntity;
import com.wendersonp.account.infrastructure.persistence.repository.AccountEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountRepositoryServiceAdapter implements AccountRepositoryDrivenPort {

    private final AccountEntityRepository accountEntityRepository;

    @Override
    public AccountModel persist(AccountModel accountModel) {
        BalanceEntity balanceEntity = new BalanceEntity(accountModel.getBalance().get());
        AccountEntity accountEntity = new AccountEntity(accountModel, balanceEntity);

        return accountEntityRepository.save(accountEntity).toModel();
    }

    @Override
    public boolean exists(UUID holderId) {
        return accountEntityRepository.existsByHolderId(holderId);
    }

    @Override
    public Optional<AccountModel> findByIdentifier(UUID identifier) {
        return accountEntityRepository.findByIdentifier(identifier).map(AccountEntity::toModel);
    }

    @Override
    public List<AccountModel> findAll() {
        return accountEntityRepository.findAll()
                .stream()
                .map(AccountEntity::toModel)
                .toList();
    }

    @Override
    public Optional<AccountModel> findByHolderId(UUID holderId) {
        return accountEntityRepository.findByHolderId(holderId).map(AccountEntity::toModel);
    }
}
