package com.vivekvarma1.moviebooking.common.customExceptionHandler;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}