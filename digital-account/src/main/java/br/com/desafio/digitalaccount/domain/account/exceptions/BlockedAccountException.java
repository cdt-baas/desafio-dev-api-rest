package br.com.desafio.digitalaccount.domain.account.exceptions;

public class BlockedAccountException extends RuntimeException {

    public BlockedAccountException(String message) {
        super(message);
    }
}
