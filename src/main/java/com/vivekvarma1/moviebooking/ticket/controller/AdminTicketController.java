package com.vivekvarma1.moviebooking.ticket.controller;

import com.vivekvarma1.moviebooking.ticket.entity.TicketVerifyResponse;
import com.vivekvarma1.moviebooking.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/tickets")
@RequiredArgsConstructor
public class AdminTicketController {

    private final TicketService ticketService;

    @PostMapping("/{ticketNumber}/check-in")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public TicketVerifyResponse checkIn(@PathVariable String ticketNumber) {
        return ticketService.checkIn(ticketNumber);
    }
}