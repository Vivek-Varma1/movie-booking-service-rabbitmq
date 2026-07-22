package com.vivekvarma1.moviebooking.show.dto.response;

import com.vivekvarma1.moviebooking.show.entity.ShowSlot;

import java.time.LocalDate;
import java.util.List;

public record ShowSeatLayoutResponse(

        Long showId,

        String movieName,

        LocalDate showDate,

        ShowSlot showSlot,

        String theatreName,

        String screenName,

        List<SeatRowResponse> rows

) {}