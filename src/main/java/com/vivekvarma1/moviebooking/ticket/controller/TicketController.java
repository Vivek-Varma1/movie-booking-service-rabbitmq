package com.vivekvarma1.moviebooking.ticket.controller;

import com.vivekvarma1.moviebooking.ticket.response.TicketResponse;
import com.vivekvarma1.moviebooking.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<TicketResponse> getTicket(
            @PathVariable Long bookingId
    ) {

        return ResponseEntity.ok(
                ticketService.getTicket(bookingId)
        );
    }

}