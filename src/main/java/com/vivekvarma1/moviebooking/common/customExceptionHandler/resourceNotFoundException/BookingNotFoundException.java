package com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.ApiException;

public class BookingNotFoundException extends ApiException {
    public BookingNotFoundException(Long bookingId) {
        super("Booking not found: " + bookingId);
    }
}