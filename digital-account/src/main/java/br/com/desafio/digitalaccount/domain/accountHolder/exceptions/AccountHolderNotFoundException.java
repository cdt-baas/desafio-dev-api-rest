package br.com.desafio.digitalaccount.domain.accountHolder.exceptions;

public class AccountHolderNotFoundException extends RuntimeException {

    public AccountHolderNotFoundException(String message) {
        super(message);
    }
}
