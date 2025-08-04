package br.com.desafio.digitalaccount.dto.account;

import br.com.desafio.digitalaccount.utils.ValidCpf;
import jakarta.validation.constraints.NotBlank;

public record AccountDeletionDTO(
        @NotBlank(message = "Account number must not be blank!")
        String number,
        @ValidCpf
        String cpf
) {
}
