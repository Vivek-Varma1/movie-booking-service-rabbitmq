package com.vivekvarma1.moviebooking.show.controller;

import com.vivekvarma1.moviebooking.show.dto.request.LockSeatsRequest;
import com.vivekvarma1.moviebooking.show.dto.response.LockSeatsResponse;
import com.vivekvarma1.moviebooking.show.dto.response.ShowSeatLayoutResponse;
import com.vivekvarma1.moviebooking.show.service.ShowSeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowSeatController {

    private final ShowSeatService showSeatService;

    @GetMapping("/{showId}/seats")
    public ShowSeatLayoutResponse getSeatLayout(
            @PathVariable Long showId
    ) {
        return showSeatService.getSeatLayout(showId);
    }
    @PostMapping("/lock")
    public ResponseEntity<LockSeatsResponse> lockSeats(
            @Valid @RequestBody LockSeatsRequest request
    ) {
        return ResponseEntity.ok(
                showSeatService.lockSeats(request)
        );
    }
    @GetMapping("/{showId}/seat-ids")
    public ResponseEntity<List<Long>> getShowSeatIds(
            @PathVariable Long showId
    ) {
        return ResponseEntity.ok(
                showSeatService.getShowSeatIds(showId)
        );
    }
}