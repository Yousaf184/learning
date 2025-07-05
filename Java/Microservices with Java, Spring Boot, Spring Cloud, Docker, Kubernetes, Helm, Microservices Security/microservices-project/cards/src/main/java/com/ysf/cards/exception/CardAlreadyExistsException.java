package com.ysf.cards.exception;

public class CardAlreadyExistsException extends RuntimeException {

    public CardAlreadyExistsException() {
        super("Card associated with the given mobile number already exists");
    }
}
