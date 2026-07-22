package com.vivekvarma1.moviebooking.common.customExceptionHandler;

public class InvalidBookingStateException extends RuntimeException {
    public InvalidBookingStateException(String message) {
        super(message);
    }
}