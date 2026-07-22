package com.vivekvarma1.moviebooking.common.customExceptionHandler;

public class SeatLockExpiredException
        extends RuntimeException {

    public SeatLockExpiredException() {
        super(
                "Seat lock has expired."
        );
    }
}