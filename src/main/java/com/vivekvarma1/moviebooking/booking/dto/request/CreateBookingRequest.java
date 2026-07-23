package com.vivekvarma1.moviebooking.booking.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateBookingRequest(

        @NotNull(message = "User id is required")
        Long userId,

        @NotNull(message = "Show id is required")
        Long showId,

        @NotEmpty(message = "At least one seat must be selected")
        List<Long> showSeatIds

) {
}