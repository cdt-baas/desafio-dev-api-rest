package br.com.desafio.digitalaccount.domain.account.exceptions;

public class AccountStatusException extends RuntimeException {

    public AccountStatusException(String message) {
        super(message);
    }
}
