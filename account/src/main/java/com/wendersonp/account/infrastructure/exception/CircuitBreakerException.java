package com.wendersonp.account.infrastructure.exception;

public class CircuitBreakerException extends RuntimeException {
    public CircuitBreakerException(String message) {
        super(message);
    }
}
