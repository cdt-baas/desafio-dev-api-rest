package com.wendersonp.account.core.ports.driven;

import com.wendersonp.account.core.model.HolderModel;

import java.util.Optional;
import java.util.UUID;

public interface HolderServiceDrivenPort {

    Optional<HolderModel> findByDocument(String documentNumber);

    Optional<HolderModel> findByIdentifier(UUID identifier);

    void removeByIdentifier(UUID identifier);

    void reactivate(UUID holderId);
}
