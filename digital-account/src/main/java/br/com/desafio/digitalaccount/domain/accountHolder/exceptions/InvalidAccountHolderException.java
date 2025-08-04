package br.com.desafio.digitalaccount.domain.accountHolder.exceptions;

public class InvalidAccountHolderException extends RuntimeException {

    public InvalidAccountHolderException(String message) {
        super(message);
    }
}
