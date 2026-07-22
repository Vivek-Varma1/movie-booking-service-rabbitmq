package com.vivekvarma1.moviebooking.show.dto.response;

import java.util.List;

public record SeatRowResponse(

        String row,

        List<ShowSeatResponse> seats

) {}