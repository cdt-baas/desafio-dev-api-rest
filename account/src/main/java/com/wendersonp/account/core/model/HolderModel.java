package com.wendersonp.account.core.model;

import com.wendersonp.account.core.model.enumeration.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HolderModel {

    private UUID identifier;

    private String maskedDocumentNumber;

    private StatusEnum status;
}
