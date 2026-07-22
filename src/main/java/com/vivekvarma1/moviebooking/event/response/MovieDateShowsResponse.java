package com.vivekvarma1.moviebooking.event.response;

import java.time.LocalDate;
import java.util.List;

public record MovieDateShowsResponse(
        MovieSummaryResponse movie,
        CitySummaryResponse city,
        LocalDate showDate,
        List<TheatreShowResponse> theatres
) {}