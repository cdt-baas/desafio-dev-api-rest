package br.com.desafio.digitalaccount.dto.account;

import java.math.BigDecimal;

public record AccountRequestDTO(
        String name,
        String cpf,
        BigDecimal initialBalance
) {
}
