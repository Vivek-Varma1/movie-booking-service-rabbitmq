package com.vivekvarma1.moviebooking.booking.dto.response;

import com.vivekvarma1.moviebooking.booking.entity.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AdminBookingResponse(
        Long bookingId,
        BookingStatus status,
        BigDecimal totalAmount,
        LocalDateTime bookedAt,
        Long userId,
        String userName,
        String userEmail,
        String userPhone,
        String movieName,
        String theatreName,
        String screenName,
        LocalDateTime showStartTime,
        List<String> seats,
        String ticketNumber        // null until the booking is confirmed
) {}