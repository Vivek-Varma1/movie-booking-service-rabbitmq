package com.vivekvarma1.moviebooking.booking.dto.response;

import java.math.BigDecimal;

public record BookingSeatResponse(

        Long showSeatId,

        String seatLabel,

        BigDecimal price

) {
}