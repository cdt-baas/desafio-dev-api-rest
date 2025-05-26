package com.wendersonp.account.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.enumClass = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = switch (object) {
            case null -> true;
            case String stringValue -> Arrays
                    .stream(enumClass.getEnumConstants())
                    .anyMatch(enumValue -> enumValue.name().equals(stringValue));
            case Enum<?> enumValue -> Arrays
                    .stream(enumClass.getEnumConstants())
                    .anyMatch(value -> value.name().equals(enumValue.name()));
            default -> false;
        };
        if (!isValid) {
            putPossibleValuesToContext(constraintValidatorContext);
        }

        return isValid;
    }

    private void putPossibleValuesToContext(ConstraintValidatorContext context) {
        String allowed = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);

        hibernateContext.addMessageParameter(
                "enumValues",
                allowed
        );
    }


}
