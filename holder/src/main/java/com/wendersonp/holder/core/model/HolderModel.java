package com.wendersonp.holder.core.model;

import com.wendersonp.holder.core.model.enumeration.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HolderModel {
    private UUID identifier;
    private byte[] documentHash;
    private String maskedDocumentNumber;
    private String name;
    private StatusEnum status;

    public HolderModel(
            byte[] documentHash,
            String maskedDocumentNumber,
            String name,
            StatusEnum status
    ) {
        this.documentHash = documentHash;
        this.maskedDocumentNumber = maskedDocumentNumber;
        this.name = name;
        this.status = status;
    }
}
