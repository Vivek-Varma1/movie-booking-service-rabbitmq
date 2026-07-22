package com.vivekvarma1.moviebooking.theatre.controller;

import com.vivekvarma1.moviebooking.theatre.dto.request.CreateScreenRequest;
import com.vivekvarma1.moviebooking.theatre.dto.response.ScreenResponse;
import com.vivekvarma1.moviebooking.theatre.dto.response.ScreenSummaryResponse;
import com.vivekvarma1.moviebooking.theatre.service.ScreenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres/{theatreId}/screens")
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService screenService;

    @PostMapping
    public ResponseEntity<ScreenResponse> createScreen(
            @PathVariable Long theatreId,
            @Valid @RequestBody CreateScreenRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        screenService.createScreen(
                                theatreId,
                                request
                        )
                );
    }
    @DeleteMapping("/{screenId}")
    public ResponseEntity<Void> deleteScreen(
            @PathVariable Long theatreId,
            @PathVariable Long screenId
    ) {

        screenService.deleteScreen(
                theatreId,
                screenId
        );

        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<ScreenSummaryResponse>> getScreensByTheatre(
            @PathVariable Long theatreId
    ) {

        return ResponseEntity.ok(
                screenService.getScreensByTheatre(
                        theatreId
                )
        );
    }
}