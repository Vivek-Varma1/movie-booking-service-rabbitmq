package com.vivekvarma1.moviebooking.booking.dto.request;

import jakarta.validation.constraints.NotNull;

public record ConfirmBookingRequest(

        @NotNull
        Long bookingId

) {
}