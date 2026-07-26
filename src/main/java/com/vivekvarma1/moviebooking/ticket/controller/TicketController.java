package com.vivekvarma1.moviebooking.ticket.controller;

import com.vivekvarma1.moviebooking.ticket.response.TicketResponse;
import com.vivekvarma1.moviebooking.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<TicketResponse> getTicket(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                ticketService.getTicket(bookingId)
        );
    }

    @GetMapping(
            value = "/{ticketId}/qr",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> getQrCode(
            @PathVariable Long ticketId) {

        return ResponseEntity.ok(
                ticketService.getQrCode(ticketId)
        );
    }

}