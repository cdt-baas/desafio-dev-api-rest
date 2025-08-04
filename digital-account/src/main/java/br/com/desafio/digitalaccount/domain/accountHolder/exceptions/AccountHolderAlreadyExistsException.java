package br.com.desafio.digitalaccount.domain.accountHolder.exceptions;

public class AccountHolderAlreadyExistsException extends RuntimeException {

    public AccountHolderAlreadyExistsException(String message) {
        super(message);
    }
}
