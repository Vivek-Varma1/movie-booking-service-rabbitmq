package com.vivekvarma1.moviebooking.theatre.controller;

import com.vivekvarma1.moviebooking.theatre.dto.request.CreateTheatreRequest;
import com.vivekvarma1.moviebooking.theatre.dto.response.TheatreResponse;
import com.vivekvarma1.moviebooking.theatre.dto.response.TheatreSummaryResponse;
import com.vivekvarma1.moviebooking.theatre.service.ScreenService;
import com.vivekvarma1.moviebooking.theatre.service.TheatreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor
public class TheatreController {

    private final TheatreService theatreService;
    private final ScreenService screenService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TheatreResponse> createTheatre(
            @Valid @RequestBody CreateTheatreRequest request
    ) {
        TheatreResponse response = theatreService.createTheatre(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{theatreId}")
    public ResponseEntity<TheatreResponse> getTheatre(
            @PathVariable Long theatreId
    ) {
        return ResponseEntity.ok(
                theatreService.getTheatre(theatreId)
        );
    }
    @GetMapping
    public ResponseEntity<List<TheatreSummaryResponse>> getAllTheatres() {
        return ResponseEntity.ok(theatreService.getAllTheatres());
    }
//    @PostMapping("/{theatreId}/screens")
//    public ResponseEntity<ScreenResponse> createScreen(
//            @PathVariable Long theatreId,
//            @Valid @RequestBody CreateScreenRequest request
//    ) {
//        ScreenResponse response =
//                screenService.createScreen(theatreId, request);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }
}