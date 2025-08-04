package br.com.desafio.digitalaccount.domain.accountHolder.exceptions;

public class CPFNotFoundException extends RuntimeException{

    public CPFNotFoundException(String message) {
        super(message);
    }
}
