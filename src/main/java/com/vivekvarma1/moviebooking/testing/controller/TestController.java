package com.vivekvarma1.moviebooking.testing.controller;

import com.vivekvarma1.moviebooking.testing.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/testing")
@RequiredArgsConstructor
public class TestController {

    private final TestService service;

    @PostMapping("/reset")

    public ResponseEntity<String> resetAllSeats() {

        service.resetAllSeats();

        return ResponseEntity.ok("All seats reset.");

    }

    @PostMapping("/reset/{seatId}")

    public ResponseEntity<String> resetSeat(

            @PathVariable Long seatId

    ) {

        service.resetSeat(seatId);

        return ResponseEntity.ok("Seat reset.");

    }

}