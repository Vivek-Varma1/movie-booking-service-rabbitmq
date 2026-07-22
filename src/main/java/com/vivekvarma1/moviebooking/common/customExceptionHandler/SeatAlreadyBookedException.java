package com.vivekvarma1.moviebooking.common.customExceptionHandler;

public class SeatAlreadyBookedException
        extends RuntimeException {

    public SeatAlreadyBookedException(
            String seatLabel
    ) {
        super(
                "Seat " + seatLabel + " is already booked."
        );
    }
}