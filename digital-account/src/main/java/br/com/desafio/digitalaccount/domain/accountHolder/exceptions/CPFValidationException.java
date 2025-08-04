package br.com.desafio.digitalaccount.domain.accountHolder.exceptions;

public class CPFValidationException extends RuntimeException {

    public CPFValidationException(String message) {
        super(message);
    }
}
