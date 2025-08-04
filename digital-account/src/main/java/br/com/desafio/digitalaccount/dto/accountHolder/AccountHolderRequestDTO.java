package br.com.desafio.digitalaccount.dto.accountHolder;

import br.com.desafio.digitalaccount.utils.ValidCpf;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountHolderRequestDTO(
        @NotNull(message = "Name cannot be null!")
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotNull(message = "CPF cannot be null!")
        @NotBlank(message = "CPF cannot be blank!")
        @ValidCpf
        String cpf
) {
}
