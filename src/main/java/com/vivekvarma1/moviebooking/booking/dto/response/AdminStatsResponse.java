package com.vivekvarma1.moviebooking.booking.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AdminStatsResponse(
        int days,
        BigDecimal revenue,
        BigDecimal revenueToday,
        long confirmedBookings,
        long ticketsSold,
        long pendingPayments,
        List<DailyPoint> daily,
        List<TopMovie> topMovies
) {
    public record DailyPoint(LocalDate date, BigDecimal revenue, long bookings) {}
    public record TopMovie(String movieName, String language, BigDecimal revenue, long bookings) {}
}