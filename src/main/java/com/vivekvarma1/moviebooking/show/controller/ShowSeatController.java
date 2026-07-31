package com.vivekvarma1.moviebooking.show.controller;

import com.vivekvarma1.moviebooking.show.dto.request.LockSeatsRequest;
import com.vivekvarma1.moviebooking.show.dto.response.LockSeatsResponse;
import com.vivekvarma1.moviebooking.show.dto.response.ShowSeatLayoutResponse;
import com.vivekvarma1.moviebooking.show.service.ShowSeatService;
import com.vivekvarma1.moviebooking.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
//    @PostMapping("/lock")
//    public ResponseEntity<LockSeatsResponse> lockSeats(
//            @Valid @RequestBody LockSeatsRequest request
//    ) {
//        return ResponseEntity.ok(
//                showSeatService.lockSeats(request)
//        );
//    }
@PostMapping("/lock")
public LockSeatsResponse lockSeats(
        @AuthenticationPrincipal User user,
        @RequestBody @Valid LockSeatsRequest request
) {
    return showSeatService.lockSeats(user, request);
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