package br.com.desafio.digitalaccount.dto.account;

import br.com.desafio.digitalaccount.utils.ValidCpf;
import jakarta.validation.constraints.NotBlank;

public record UpdateAccountStatusRequestDTO(
        @NotBlank(message = "Account number cannot be blank!")
        String accountNumber,
        @ValidCpf
        String accountHolderCpf
) {}
