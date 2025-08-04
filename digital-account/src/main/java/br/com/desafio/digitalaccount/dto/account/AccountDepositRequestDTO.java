package br.com.desafio.digitalaccount.dto.account;

import br.com.desafio.digitalaccount.utils.ValidCpf;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountDepositRequestDTO(
        @NotBlank(message = "Account number cannot be blank!")
        String accountNumber,
        @NotBlank(message = "CPF cannot be blank!")
        @ValidCpf
        String accountHolderCpf,
        @NotNull(message = "Value amount is required!")
        @DecimalMin(value = "0.01", message = "The deposit value must be higher than zero!")
        BigDecimal amount
) {
}
