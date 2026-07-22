package com.vivekvarma1.moviebooking.show.controller;

import com.vivekvarma1.moviebooking.show.dto.request.CreateShowRequest;
import com.vivekvarma1.moviebooking.show.dto.response.ShowResponse;
import com.vivekvarma1.moviebooking.show.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @PostMapping
    public ResponseEntity<ShowResponse> createShow(
            @Valid
            @RequestBody
            CreateShowRequest request
    ) {

        ShowResponse response =
                showService.createShow(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{showId}")
    public ResponseEntity<ShowResponse> getShow(
            @PathVariable Long showId
    ) {

        return ResponseEntity.ok(
                showService.getShow(showId)
        );
    }
    @GetMapping
    public ResponseEntity<List<ShowResponse>> getAllShows() {

        return ResponseEntity.ok(
                showService.getAllShows()
        );
    }

    @DeleteMapping("/{showId}")
    public ResponseEntity<Void> deleteShow(
            @PathVariable Long showId
    ) {

        showService.deleteShow(showId);

        return ResponseEntity.noContent().build();
    }
}