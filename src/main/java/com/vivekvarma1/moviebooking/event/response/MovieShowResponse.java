package com.vivekvarma1.moviebooking.event.response;

import java.util.List;

public record MovieShowResponse(
        MovieSummaryResponse movie,
        CitySummaryResponse city,
        List<TheatreShowResponse> theatres
) {}