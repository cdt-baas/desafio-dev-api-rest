package br.com.desafio.digitalaccount.dto.account;

import br.com.desafio.digitalaccount.domain.account.AccountStatus;

import java.math.BigDecimal;

public record AccountResponseDTO(
        BigDecimal balance,
        String number,
        String agency,
        AccountStatus status,
        String accountHolderName
) {
}
