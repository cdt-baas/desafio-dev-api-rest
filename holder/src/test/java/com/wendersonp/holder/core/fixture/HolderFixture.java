package com.wendersonp.holder.core.fixture;

import com.wendersonp.holder.core.model.HolderModel;
import com.wendersonp.holder.core.model.enumeration.StatusEnum;

import java.nio.charset.StandardCharsets;

public class HolderFixture {

    public static HolderModel createSuccessfulCase() {
        return HolderModel
                .builder()
                .name("Wenderson")
                .status(StatusEnum.ACTIVE)
                .maskedDocumentNumber("646#####032")
                .documentHash("hashSomething".getBytes(StandardCharsets.UTF_8))
                .build();
    }
}
