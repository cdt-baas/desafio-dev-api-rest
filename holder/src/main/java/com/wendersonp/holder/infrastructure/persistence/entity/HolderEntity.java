package com.wendersonp.holder.infrastructure.persistence.entity;

import com.wendersonp.holder.core.model.HolderModel;
import com.wendersonp.holder.core.model.enumeration.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_holder")
public class HolderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID identifier;

    @Column(name = "document_hash", unique = true, nullable = false, columnDefinition = "BYTEA")
    private byte[] documentHash;

    @Column(name = "masked_document_number", nullable = false)
    private String maskedDocumentNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEnum status;

    public HolderEntity(HolderModel holderModel) {
        this.documentHash = holderModel.getDocumentHash();
        this.maskedDocumentNumber = holderModel.getMaskedDocumentNumber();
        this.name = holderModel.getName();
        this.status = holderModel.getStatus();
    }

    public HolderModel toModel() {
        return HolderModel.builder()
                .identifier(identifier)
                .documentHash(documentHash)
                .maskedDocumentNumber(maskedDocumentNumber)
                .name(name)
                .status(status)
                .build();
    }

}
