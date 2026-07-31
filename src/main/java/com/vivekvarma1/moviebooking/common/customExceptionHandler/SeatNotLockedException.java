package com.vivekvarma1.moviebooking.common.customExceptionHandler;

public class SeatNotLockedException extends RuntimeException {

    public SeatNotLockedException(String message) {
        super(message);
    }
}