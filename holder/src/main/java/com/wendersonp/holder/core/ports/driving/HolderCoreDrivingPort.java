package com.wendersonp.holder.core.ports.driving;

import com.wendersonp.holder.core.exceptions.BusinessException;
import com.wendersonp.holder.core.model.HolderModel;
import com.wendersonp.holder.core.model.HolderRequestModel;

import java.util.List;
import java.util.UUID;

public interface HolderCoreDrivingPort {

    HolderModel save(HolderRequestModel holderModel);

    HolderModel findByDocument(String documentNumber) throws BusinessException;

    HolderModel findByIdentifier(UUID identifier);

    List<HolderModel> findAll();

    void deleteByIdentifier(UUID identifier);

    void reactivate(UUID identifier);
}
