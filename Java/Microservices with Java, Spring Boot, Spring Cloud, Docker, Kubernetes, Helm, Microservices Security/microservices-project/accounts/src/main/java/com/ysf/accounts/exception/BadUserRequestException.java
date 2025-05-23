package com.ysf.accounts.exception;

public class BadUserRequestException extends RuntimeException {
    public BadUserRequestException(String message) {
        super(message);
    }
}
