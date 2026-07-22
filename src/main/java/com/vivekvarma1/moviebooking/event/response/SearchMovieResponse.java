package com.vivekvarma1.moviebooking.event.response;

public record SearchMovieResponse(
        Long id,
        String movieName,
        String posterUrl
) {}