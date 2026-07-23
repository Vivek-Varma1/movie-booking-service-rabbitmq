package com.vivekvarma1.moviebooking.ticket.response;

import java.time.LocalDateTime;
import java.util.List;

public record TicketResponse(

        String ticketNumber,

        String movie,

        String theatre,

        String screen,

        LocalDateTime showTime,

        List<String> seats,

        LocalDateTime issuedAt,

        String qrCode

) {
}