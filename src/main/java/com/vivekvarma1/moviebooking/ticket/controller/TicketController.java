package com.vivekvarma1.moviebooking.ticket.controller;

import com.vivekvarma1.moviebooking.ticket.response.TicketResponse;
import com.vivekvarma1.moviebooking.ticket.service.TicketService;
import com.vivekvarma1.moviebooking.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/booking/{bookingId}")
    public TicketResponse getTicket(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal User user
    ) {
        return ticketService.getTicket(bookingId, user.getId());
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
//    @GetMapping("/me")
//    public ResponseEntity<TicketResponse> myTickets(
//            @AuthenticationPrincipal User user
//    ) {
//        return ResponseEntity.ok(
//                ticketService.getTicket(booZuser.getId())
//        );
//    }
//    @GetMapping("/all/me")
//    public ResponseEntity<List<TicketResponse>> getMyTickets(
//            @AuthenticationPrincipal User user
//    ) {
//        return ResponseEntity.ok(
//                ticketService.getTicketsByUser(user.getId())
//        );
//    }
}