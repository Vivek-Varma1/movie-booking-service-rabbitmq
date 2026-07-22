package com.vivekvarma1.moviebooking.booking.dto.response;

import com.vivekvarma1.moviebooking.booking.entity.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(

        Long bookingId,

        Long showId,

        String movieName,

        String theatreName,

        String screenName,

        BookingStatus bookingStatus,

        BigDecimal totalAmount,

        LocalDateTime bookedAt,

        List<BookingSeatResponse> seats

) {
}