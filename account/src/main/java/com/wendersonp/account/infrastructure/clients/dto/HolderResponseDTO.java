package com.wendersonp.account.infrastructure.clients.dto;

import com.wendersonp.account.core.model.HolderModel;
import com.wendersonp.account.core.model.enumeration.StatusEnum;

import java.util.UUID;

public record HolderResponseDTO(UUID identifier, String maskedDocumentNumber, String name, StatusEnum status) {

    public HolderModel toModel() {
        return HolderModel.builder()
                .identifier(identifier)
                .maskedDocumentNumber(maskedDocumentNumber)
                .status(status)
                .build();
    }
}
