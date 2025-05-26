package com.wendersonp.holder.application.dto;

import com.wendersonp.holder.core.model.HolderModel;
import com.wendersonp.holder.core.model.enumeration.StatusEnum;

import java.util.UUID;

public record HolderResponseDTO(UUID identifier, String maskedDocumentNumber, String name, StatusEnum status) {

    public HolderResponseDTO(HolderModel holderModel) {
        this(
                holderModel.getIdentifier(),
                holderModel.getMaskedDocumentNumber(),
                holderModel.getName(),
                holderModel.getStatus()
        );
    }
}
