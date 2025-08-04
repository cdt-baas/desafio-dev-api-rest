package br.com.desafio.digitalaccount.domain.account.exceptions;

public class DailyLimitAlreadyReachedException extends RuntimeException {

    public DailyLimitAlreadyReachedException(String message) {
        super(message);
    }
}
