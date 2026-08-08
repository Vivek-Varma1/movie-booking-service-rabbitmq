package com.vivekvarma1.moviebooking.event.controller;

import com.vivekvarma1.moviebooking.event.request.CreateMovieRequest;
import com.vivekvarma1.moviebooking.event.request.UpdateMovieRequest;
import com.vivekvarma1.moviebooking.event.response.MovieDateShowsResponse;
import com.vivekvarma1.moviebooking.event.response.MovieResponse;
import com.vivekvarma1.moviebooking.event.response.MovieShowResponse;
import com.vivekvarma1.moviebooking.event.response.MovieSummaryResponse;
import com.vivekvarma1.moviebooking.event.serivce.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    /*
     * Admin APIs
     */

//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<MovieResponse> createMovie(
//            @Valid @RequestBody CreateMovieRequest request
//    ) {
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(movieService.createMovie(request));
//    }
//
//    @PatchMapping("/{movieId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<MovieResponse> updateMovie(
//            @PathVariable Long movieId,
//            @Valid @RequestBody UpdateMovieRequest request
//    ) {
//        return ResponseEntity.ok(
//                movieService.updateMovie(
//                        movieId,
//                        request
//                )
//        );
//    }
@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<MovieResponse> createMovie(
        @RequestPart("request") @Valid CreateMovieRequest request,
        @RequestPart("poster") MultipartFile posterFile
) {
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(movieService.createMovie(request, posterFile));
}

    @PatchMapping(value = "/{movieId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponse> updateMovie(
            @PathVariable Long movieId,
            @RequestPart(value = "request", required = false) @Valid UpdateMovieRequest request,
            @RequestPart(value = "poster", required = false) MultipartFile posterFile
    ) {
        return ResponseEntity.ok(
                movieService.updateMovie(movieId, request, posterFile)
        );
    }
    @DeleteMapping("/{movieId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(
            @PathVariable Long movieId
    ) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }

    /*
     * Homepage Movies
     */

    @GetMapping
    public ResponseEntity<List<MovieSummaryResponse>> getAllMovies(
            @RequestParam("cityId") Long cityId
    ) {
        return ResponseEntity.ok(
                movieService.getAllMovies(cityId)
        );
    }

    /*
     * Movie details page
     */

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieResponse> getMovieById(
            @PathVariable Long movieId
    ) {
        return ResponseEntity.ok(
                movieService.getMovieById(movieId)
        );
    }

    /*
     * Search API
     */

    @GetMapping("/search")
    public ResponseEntity<List<MovieSummaryResponse>> searchMovies(
            @RequestParam String query,
            @RequestParam Long cityId
    ) {
        return ResponseEntity.ok(
                movieService.searchMovies(
                        query,
                        cityId
                )
        );
    }

    /*
     * Book Tickets Page
     * Returns dates only
     */

    @GetMapping("/{movieId}/show-dates")
    public ResponseEntity<List<LocalDate>> getMovieShowDates(
            @PathVariable Long movieId,
            @RequestParam Long cityId
    ) {
        return ResponseEntity.ok(
                movieService.getMovieShowDates(
                        movieId,
                        cityId
                )
        );
    }

    /*
     * User selects a date
     */

    @GetMapping("/{movieId}/shows")
    public ResponseEntity<MovieDateShowsResponse> getMovieShows(
            @PathVariable Long movieId,
            @RequestParam Long cityId,
            @RequestParam LocalDate showDate
    ) {
        return ResponseEntity.ok(
                movieService.getMovieShows(
                        movieId,
                        cityId,
                        showDate
                )
        );
    }
}