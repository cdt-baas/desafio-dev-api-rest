package com.wendersonp.account.util;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        in = ParameterIn.QUERY,
        description = "Índice da página, iniciando com 0 (0,...,N)",
        name = "page",
        schema = @Schema(
                type = "integer",
                defaultValue = "0"
        )
)
@Parameter(
        in = ParameterIn.QUERY,
        description = "O tamanho da página a ser retornada",
        name = "size",
        schema = @Schema(
                type = "integer",
                defaultValue = "10"
        )
)
@Parameter(
        in = ParameterIn.QUERY,
        description = "O criterio de ordenação no formato: propriedade,ordem(asc|desc), " +
                "a ordem padrão é ascendente, " +
                "multiplos criterios de ordenação podem ser informados ",
        name = "sort",
        array = @ArraySchema(
                schema = @Schema(
                        type = "string"
                )
        )
)
public @interface PageableAsQueryParamPortuguese {
}
