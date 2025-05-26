package com.wendersonp.account.application.dto;

import com.wendersonp.account.core.model.enumeration.MovementType;
import com.wendersonp.account.util.EnumValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record MovementRequestDTO(
        @NotNull
        UUID accountIdentifier,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        @EnumValue(MovementType.class)
        String type,

        String description) {
}
