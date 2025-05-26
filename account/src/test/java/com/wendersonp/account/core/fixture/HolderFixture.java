package com.wendersonp.account.core.fixture;

import com.wendersonp.account.core.model.HolderModel;
import com.wendersonp.account.core.model.enumeration.StatusEnum;

import java.util.UUID;

public class HolderFixture {

    public static HolderModel getHolderModel() {
        return HolderModel.builder()
                .identifier(UUID.randomUUID())
                .maskedDocumentNumber("646######32")
                .status(StatusEnum.ACTIVE)
                .build();
    }
}
