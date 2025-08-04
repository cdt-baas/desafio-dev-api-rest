package br.com.desafio.digitalaccount.dto.operation;

import br.com.desafio.digitalaccount.domain.operation.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OperationResponseDTO(
        BigDecimal amount,
        LocalDateTime dateTime,
        OperationType operationType,
        String accountNumber
) {
}
