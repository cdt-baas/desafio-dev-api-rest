
package com.wendersonp.account.infrastructure.web.v1.controller.docs;

import com.wendersonp.account.application.dto.MovementResponseDTO;
import com.wendersonp.account.infrastructure.web.v1.handler.ApiError;
import com.wendersonp.account.util.PageableAsQueryParamPortuguese;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Movement", description = "Operações de movimentação de conta")
public interface MovementControllerV1ApiDocs {

    @Operation(
            summary = "Busca movimentações dentro de um período",
            description = "Retorna lista paginada de movimentos entre as datas informadas para uma conta, " +
                    "a ordem padrão é a data da movimentação, da mais recente para a mais antiga"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Movimentações retornadas com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MovementResponseDTO.class))
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Parâmetros inválidos (datas fora de ordem, formato inválido ou datas inválidas)",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Conta não encontrada",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)
            )
    )
    @PageableAsQueryParamPortuguese
    ResponseEntity<List<MovementResponseDTO>> getMovementsWithinAPeriod(
            @Parameter(description = "Data de início no formato ISO (yyyy-MM-dd)", required = true)
            LocalDate beginningDate,

            @Parameter(description = "Data de fim no formato ISO (yyyy-MM-dd)", required = true)
            LocalDate endingDate,

            @Parameter(description = "UUID da conta", required = true)
            UUID accountIdentifier,

            @Parameter(description = "Parâmetros de paginação e ordenação", required = false, hidden = true)
            Pageable pageable
    );
}
