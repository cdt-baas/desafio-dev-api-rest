package com.wendersonp.account.infrastructure.persistence.converters;

import com.wendersonp.account.core.model.enumeration.MovementType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MovementTypeEnumConverter implements AttributeConverter<MovementType, String> {
    @Override
    public String convertToDatabaseColumn(MovementType movementType) {
        if (movementType == null) {
            return null;
        }
        return movementType.value();
    }

    @Override
    public MovementType convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        return MovementType.fromValue(s);
    }


}
