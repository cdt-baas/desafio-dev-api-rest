package com.wendersonp.account.infrastructure.persistence.repository;


import com.wendersonp.account.infrastructure.persistence.entity.BalanceEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BalanceEntityRepository extends JpaRepository<BalanceEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    BalanceEntity findByIdentifier(UUID identifier);
}
