package com.vivekvarma1.moviebooking.event.request;

import com.vivekvarma1.moviebooking.event.entity.Certificate;
import com.vivekvarma1.moviebooking.event.entity.Genre;
import com.vivekvarma1.moviebooking.event.entity.Language;
import com.vivekvarma1.moviebooking.event.entity.MovieStatus;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
public record CreateMovieRequest(

        @NotBlank(message = "Movie name is required")
        @Size(max = 255)
        String movieName,

        @NotNull
        @Min(30)
        @Max(500)
        Integer durationInMinutes,

        @NotNull
        Language language,

        @NotEmpty
        Set<Genre> genres,

        @NotNull
        Certificate certificate,

        @NotNull
        MovieStatus movieStatus,

        @NotNull
        LocalDate releaseDate,

        @Size(max = 1000)
        String posterUrl,

        @Size(max = 1000)
        String trailerUrl,

        @Size(max = 2000)
        String synopsis
) {}