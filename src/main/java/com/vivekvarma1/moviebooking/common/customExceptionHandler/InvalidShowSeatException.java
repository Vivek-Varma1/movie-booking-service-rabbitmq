package com.vivekvarma1.moviebooking.common.customExceptionHandler;

public class InvalidShowSeatException extends RuntimeException {

    public InvalidShowSeatException() {
        super("One or more seats are invalid for the selected show.");
    }
}