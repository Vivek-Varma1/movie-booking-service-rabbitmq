package com.vivekvarma1.moviebooking.booking.controller;

import com.vivekvarma1.moviebooking.booking.dto.request.CreateBookingRequest;
import com.vivekvarma1.moviebooking.booking.dto.response.BookingResponse;
import com.vivekvarma1.moviebooking.booking.service.BookingService;
import com.vivekvarma1.moviebooking.ticket.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(
            @RequestBody @Valid CreateBookingRequest request
    ) {
        return bookingService.createBooking(request);
    }

    @PostMapping("/{bookingId}/confirm")
    public BookingResponse confirmBooking(
            @PathVariable Long bookingId
    ) {
        return bookingService.confirmBooking(bookingId);
    }

    @PostMapping("/{bookingId}/cancel")
    public BookingResponse cancelBooking(
            @PathVariable Long bookingId
    ) {
        return bookingService.cancelBooking(bookingId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponse getBooking(
            @PathVariable Long bookingId
    ) {
        return bookingService.getBooking(bookingId);
    }
    @GetMapping(
            value = "/{ticketId}/qr",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> getQrCode(
            @PathVariable Long ticketId
    ) {

        return ResponseEntity.ok(
                ticketService.getQrCode(ticketId)
        );

    }

    @GetMapping("/users/{userId}")
    public List<BookingResponse> getBookingsByUser(
            @PathVariable Long userId
    ) {
        return bookingService.getUserBookings(userId);
    }

}