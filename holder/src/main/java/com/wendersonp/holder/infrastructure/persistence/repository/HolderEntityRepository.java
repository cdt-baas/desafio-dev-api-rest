package com.wendersonp.holder.infrastructure.persistence.repository;

import com.wendersonp.holder.core.model.enumeration.StatusEnum;
import com.wendersonp.holder.infrastructure.persistence.entity.HolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HolderEntityRepository extends JpaRepository<HolderEntity, UUID> {

    Optional<HolderEntity> findByDocumentHash(byte[] documentHash);

    List<HolderEntity> findByStatus(StatusEnum status);
}
