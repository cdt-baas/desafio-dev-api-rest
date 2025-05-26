package com.wendersonp.holder.core.ports.driven;

import com.wendersonp.holder.core.model.HolderModel;
import com.wendersonp.holder.core.model.enumeration.StatusEnum;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HolderRepositoryDrivenPort {

    HolderModel persist(HolderModel holderModel);

    Optional<HolderModel> findByDocumentHash(byte[] documentHash);

    Optional<HolderModel> findByIdentifier(UUID identifier);

    List<HolderModel> findAllByStatus(StatusEnum statusEnum);

    HolderModel setStatus(UUID identifier, StatusEnum status);
}
