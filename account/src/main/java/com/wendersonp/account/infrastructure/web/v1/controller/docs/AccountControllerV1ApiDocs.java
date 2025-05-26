
package com.wendersonp.account.infrastructure.web.v1.controller.docs;

import com.wendersonp.account.application.dto.AccountResponseDTO;
import com.wendersonp.account.application.dto.BalanceResponseDTO;
import com.wendersonp.account.application.dto.MovementRequestDTO;
import com.wendersonp.account.infrastructure.web.v1.handler.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;
import java.util.UUID;

@Tag(name = "Account", description = "Operações de gerenciamento de conta")
public interface AccountControllerV1ApiDocs {

    @Operation(summary = "Cria uma nova conta", description = "Cria uma conta para o CPF informado, dado portador existente")
    @ApiResponse(responseCode = "201", description = "Conta criada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "CPF inválido",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    AccountResponseDTO createAccount(
            @Valid
            @CPF
            @Parameter(description = "Número do documento (CPF)", required = true)
            String documentNumber
    );

    @Operation(summary = "Busca conta por identificador", description = "Retorna detalhes da conta pelo UUID")
    @ApiResponse(responseCode = "200", description = "Conta encontrada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Conta não encontrada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    AccountResponseDTO findByIdentifier(
            @Parameter(description = "UUID da conta", required = true)
            UUID identifier
    );

    @Operation(summary = "Realiza movimentação de saldo", description = "Depósito ou saque em uma conta")
    @ApiResponse(responseCode = "200", description = "Movimentação realizada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BalanceResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados de movimentação inválidos",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Conta não encontrada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    BalanceResponseDTO makeMovementOnBalance(
            @Valid
            @RequestBody(
                    description = "Detalhes da movimentação, para o campo type, use DEPOSIT ou WITHDRAW", required = true,
                    content = @Content(schema = @Schema(implementation = MovementRequestDTO.class))
            )
            MovementRequestDTO movementRequestDTO
    );

    @Operation(summary = "Bloqueia conta", description = "Define status BLOCKED na conta informada")
    @ApiResponse(responseCode = "200", description = "Conta bloqueada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Conta não encontrada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    AccountResponseDTO blockAccount(
            @Parameter(description = "UUID da conta", required = true)
            UUID identifier
    );

    @Operation(summary = "Desbloqueia conta", description = "Define status UNBLOCKED na conta informada")
    @ApiResponse(responseCode = "200", description = "Conta desbloqueada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Conta não encontrada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    AccountResponseDTO unblockAccount(
            @Parameter(description = "UUID da conta", required = true)
            UUID identifier
    );

    @Operation(summary = "Lista todas as contas", description = "Retorna lista de todas as contas")
    @ApiResponse(responseCode = "200", description = "Lista de contas",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AccountResponseDTO.class))))
    List<AccountResponseDTO> findAll();

    @Operation(summary = "Fecha conta", description = "Exclui/fecha a conta informada, é realizada uma exclusão lógica da conta")
    @ApiResponse(responseCode = "204", description = "Conta fechada com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
    void closeAccount(
            @Parameter(description = "UUID da conta", required = true)
            UUID identifier
    );
}
