package com.vivekvarma1.moviebooking.event.request;

import com.vivekvarma1.moviebooking.event.entity.MovieStatus;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateMovieRequest(

        MovieStatus movieStatus,

        @Size(max = 1000)
        String posterUrl,

        @Size(max = 1000)
        String trailerUrl,

        @Size(max = 2000)
        String synopsis
) {}