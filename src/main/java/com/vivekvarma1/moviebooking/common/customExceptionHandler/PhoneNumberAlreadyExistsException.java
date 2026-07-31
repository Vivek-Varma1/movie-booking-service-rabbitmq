package com.vivekvarma1.moviebooking.common.customExceptionHandler;

public class PhoneNumberAlreadyExistsException extends RuntimeException {

    public PhoneNumberAlreadyExistsException(String message) {
        super(message);
    }
}