package com.wendersonp.account.infrastructure.persistence.repository;

import com.wendersonp.account.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountEntityRepository extends JpaRepository<AccountEntity, UUID> {

    Boolean existsByHolderId(UUID holderId);

    @EntityGraph(value = "account-entity-with-balance-identifier",
            attributePaths = {
                    "balance.identifier"
            }
    )
    Optional<AccountEntity> findByIdentifier(UUID identifier);

    Optional<AccountEntity> findByHolderId(UUID holderId);
}
