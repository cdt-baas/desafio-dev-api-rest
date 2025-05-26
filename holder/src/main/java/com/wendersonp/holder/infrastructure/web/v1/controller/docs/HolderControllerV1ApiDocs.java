
package com.wendersonp.holder.infrastructure.web.v1.controller.docs;

import com.wendersonp.holder.application.dto.HolderRequestDTO;
import com.wendersonp.holder.application.dto.HolderResponseDTO;
import com.wendersonp.holder.infrastructure.web.v1.handler.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "Portador", description = "Operações de gerenciamento de portador")
public interface HolderControllerV1ApiDocs {

    @Operation(summary = "Cria um novo portador", description = "Salva um portador com os dados informados")
    @ApiResponse(responseCode = "201", description = "Portador criado com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HolderResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados do portador inválidos",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    @RequestBody(description = "Dados para criação do portador", required = true,
            content = @Content(schema = @Schema(implementation = HolderRequestDTO.class)))
    HolderResponseDTO save(HolderRequestDTO holderRequestDTO);

    @Operation(summary = "Busca portador por documento", description = "Retorna um portador pelo número de documento")
    @ApiResponse(responseCode = "200", description = "Portador encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HolderResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Número de documento inválido",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Portador não encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    HolderResponseDTO findByDocument(
            @Parameter(description = "Número do documento", required = true)
            String documentNumber
    );

    @Operation(summary = "Busca portador por identificador", description = "Retorna um portador pelo UUID")
    @ApiResponse(responseCode = "200", description = "Portador encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HolderResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Portador não encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    HolderResponseDTO findByIdentifier(
            @Parameter(description = "UUID do portador", required = true)
            UUID identifier
    );

    @Operation(summary = "Deleta portador por identificador", description = "Remove o portador informado pelo UUID")
    @ApiResponse(responseCode = "204", description = "Portador deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Portador não encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    void deleteByIdentifier(
            @Parameter(description = "UUID do portador", required = true)
            UUID identifier
    );

    @Operation(summary = "Lista todos os portadores", description = "Retorna lista de todos os portadores cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de portadores",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = HolderResponseDTO.class))))
    List<HolderResponseDTO> findAll();

    @Operation(summary = "Reactiva portador", description = "Ativa novamente um portador previamente desativado")
    @ApiResponse(responseCode = "204", description = "Portador reativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Portador não encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    void reactivate(
            @Parameter(description = "UUID do portador", required = true)
            UUID identifier
    );
}
