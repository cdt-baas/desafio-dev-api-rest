package br.com.desafio.digitalaccount.dto;

import br.com.desafio.digitalaccount.utils.ValidCpf;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record StatementRequestDTO(
        @NotBlank
        String accountNumber,
        @NotBlank
        @ValidCpf
        String accountHolderCpf,
        @NotNull(message = "A data de início do período é obrigatória.")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // Formato YYYY-MM-DD
        LocalDate startDate,
        @NotNull(message = "A data de fim do período é obrigatória.")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate
) {}
