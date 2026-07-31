package com.vivekvarma1.moviebooking.common.customExceptionHandler;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}