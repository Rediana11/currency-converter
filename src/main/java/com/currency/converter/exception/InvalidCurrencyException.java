package com.currency.converter.exception;

public class InvalidCurrencyException extends RuntimeException {

    public InvalidCurrencyException(String message) {
        super(message);
    }

    public InvalidCurrencyException() {
    }

    public InvalidCurrencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCurrencyException(Throwable cause) {
        super(cause);
    }

    public InvalidCurrencyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
