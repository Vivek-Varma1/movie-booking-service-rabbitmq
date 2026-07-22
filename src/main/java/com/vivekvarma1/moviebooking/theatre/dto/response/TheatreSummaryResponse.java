package com.vivekvarma1.moviebooking.theatre.dto.response;

public record TheatreSummaryResponse(
        Long id,
        String name,
        String address,
        Long cityId,
        String cityName
) {}