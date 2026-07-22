package com.vivekvarma1.moviebooking.booking.controller;

import com.vivekvarma1.moviebooking.booking.dto.BookingRequest;
import com.vivekvarma1.moviebooking.user.entity.User;
import com.vivekvarma1.moviebooking.booking.service.BookingService;
import com.vivekvarma1.moviebooking.show.service.ShowService;
import com.vivekvarma1.moviebooking.theatre.service.TheatreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final ShowService showService;
    private final BookingService bookingService;
    private final TheatreService theatreService;

    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody BookingRequest request) {
        User user = new User(request.getUserName(), request.getUserEmail());
        String bookingId = bookingService.createBooking(user, request.getShowId(), request.getSeatIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingId);
    }
}