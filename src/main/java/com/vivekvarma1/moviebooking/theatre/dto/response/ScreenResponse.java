package com.vivekvarma1.moviebooking.theatre.dto.response;

import java.util.List;

public record ScreenResponse(

        Long id,
        String name,
        Integer totalSeats,
        List<SeatResponse> seats

) {}