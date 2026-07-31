package com.vivekvarma1.moviebooking.common.customExceptionHandler;

public class BookingOwnershipException extends ApiException {
    public BookingOwnershipException() {
        super("This booking does not belong to you");
    }
}