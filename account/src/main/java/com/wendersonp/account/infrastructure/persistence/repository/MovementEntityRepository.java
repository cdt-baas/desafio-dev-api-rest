package com.wendersonp.account.infrastructure.persistence.repository;

import com.wendersonp.account.infrastructure.persistence.entity.MovementEntity;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface MovementEntityRepository extends JpaRepository<MovementEntity, UUID> {

    @Query(nativeQuery = true,
            value = "SELECT SUM(amount) FROM tb_movement " +
                    "WHERE fk_account_id = :accountIdentifier AND " +
                    "created_at BETWEEN :startDate AND :endDate AND " +
                    "type = :type"
    )
    BigDecimal totalMovementBetweenTwoDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("accountIdentifier") UUID accountIdentifier,
            @Param("type") String type
    );

    Page<MovementEntity> findByAccountIdentifierAndCreatedAtBetween(UUID accountIdentifier, LocalDateTime beginningDate, LocalDateTime endingDate, Pageable pageable);
}
