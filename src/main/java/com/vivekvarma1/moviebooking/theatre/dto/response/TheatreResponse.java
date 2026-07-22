package com.vivekvarma1.moviebooking.theatre.dto.response;

import java.util.List;

public record TheatreResponse(
        Long id,
        String name,
        String address,
        Long cityId,
        String cityName,
        List<ScreenSummaryResponse> screens
) {}