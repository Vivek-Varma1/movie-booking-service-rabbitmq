package com.vivekvarma1.moviebooking.event.response;

import com.vivekvarma1.moviebooking.show.dto.response.ShowResponse;

import java.util.List;

public record TheatreShowResponse(
        Long theatreId,
        String theatreName,
        String address,
        List<ShowResponse> shows
) {}