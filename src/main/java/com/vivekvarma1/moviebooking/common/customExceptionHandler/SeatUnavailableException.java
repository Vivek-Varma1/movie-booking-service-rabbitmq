package com.vivekvarma1.moviebooking.common.customExceptionHandler;

public class SeatUnavailableException extends RuntimeException {

    public SeatUnavailableException(String seatLabel) {
        super("Sorry! Seat " + seatLabel
                + " is no longer available. Please select another seat.");
    }

    public SeatUnavailableException() {
        super("Sorry! One or more selected seats are no longer available. Please select other seats.");
    }
}