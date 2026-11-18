package com.vivekvarma1.moviebooking.ticket.entity;

import java.time.LocalDateTime;
import java.util.List;

public record TicketVerifyResponse(
        String ticketNumber,
        String status,              // VALID | USED | EXPIRED | INVALID
        String movieName,
        String theatreName,
        String screenName,
        LocalDateTime showStartTime,
        List<String> seats,
        LocalDateTime usedAt
) {}