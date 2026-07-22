package com.vivekvarma1.moviebooking.common.customExceptionHandler;

public class SeatAlreadyLockedException extends RuntimeException {

    public SeatAlreadyLockedException(String seatLabel) {
        super("Seat " + seatLabel + " is already locked.");
    }
}