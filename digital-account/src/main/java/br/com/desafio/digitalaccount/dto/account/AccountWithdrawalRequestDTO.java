package br.com.desafio.digitalaccount.dto.account;

import br.com.desafio.digitalaccount.utils.ValidCpf;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountWithdrawalRequestDTO(
        @NotBlank(message = "Account number cannot be blank!")
        String accountNumber,
        @ValidCpf
        @NotBlank(message = "Cpf cannot be blank!")
        String accountHolderCpf,
        @NotNull(message = "The amount of the withdrawal must not be null.")
        @DecimalMin(value = "0.01", message = "The withdrawal amount must be greater then zero")
        BigDecimal amount
) {
}
