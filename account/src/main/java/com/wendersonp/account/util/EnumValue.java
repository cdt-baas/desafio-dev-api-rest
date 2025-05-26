package com.wendersonp.account.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = EnumValueValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {

    Class<? extends Enum<?>> value();

    String message() default "valor inválido, deve ser um dos seguintes: {enumValues}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
