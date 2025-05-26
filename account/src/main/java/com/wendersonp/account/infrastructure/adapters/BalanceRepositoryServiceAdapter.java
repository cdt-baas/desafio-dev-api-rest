package com.wendersonp.account.infrastructure.adapters;

import com.wendersonp.account.core.model.BalanceModel;
import com.wendersonp.account.core.ports.driven.BalanceRepositoryDrivenPort;
import com.wendersonp.account.infrastructure.persistence.entity.BalanceEntity;
import com.wendersonp.account.infrastructure.persistence.repository.BalanceEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceRepositoryServiceAdapter implements BalanceRepositoryDrivenPort {

    private final BalanceEntityRepository balanceRepository;

    @Override
    public BalanceModel persist(BalanceModel balanceModel) {
        var balanceEntity = new BalanceEntity(balanceModel);
        return balanceRepository.save(balanceEntity).toModel();
    }

    @Override
    public BalanceModel findByIdentifier(UUID identifier) {
        return balanceRepository.findByIdentifier(identifier).toModel();
    }

    @Override
    public void delete(UUID balanceIdentifier) {
        balanceRepository.deleteById(balanceIdentifier);
    }
}
