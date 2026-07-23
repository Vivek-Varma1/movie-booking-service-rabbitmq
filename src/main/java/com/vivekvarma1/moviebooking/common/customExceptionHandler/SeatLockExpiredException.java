package com.vivekvarma1.moviebooking.common.customExceptionHandler;

public class SeatLockExpiredException extends RuntimeException {

    public SeatLockExpiredException(String seatLabel) {
        super("Seat " + seatLabel + " lock has expired.");
    }
}