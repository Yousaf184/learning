package com.ysf.accounts.exception;

public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException() {
        super("Customer already has an existing account");
    }
}
