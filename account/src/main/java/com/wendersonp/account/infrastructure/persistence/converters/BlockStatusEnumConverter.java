package com.wendersonp.account.infrastructure.persistence.converters;

import com.wendersonp.account.core.model.enumeration.BlockStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BlockStatusEnumConverter implements AttributeConverter<BlockStatus, String> {
    @Override
    public String convertToDatabaseColumn(BlockStatus blockStatus) {
        if (blockStatus == null) {
            return null;
        }
        return blockStatus.value();
    }

    @Override
    public BlockStatus convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return BlockStatus.fromValue(s);
    }
}
