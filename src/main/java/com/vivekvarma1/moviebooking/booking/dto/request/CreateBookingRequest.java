package com.vivekvarma1.moviebooking.booking.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateBookingRequest(

        @NotNull
        Long userId,

        @NotNull
        Long showId,

        @NotEmpty
        List<Long> showSeatIds

) {
}