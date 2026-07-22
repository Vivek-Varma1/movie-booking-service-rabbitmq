package com.vivekvarma1.moviebooking.show.dto.response;

import com.vivekvarma1.moviebooking.show.entity.ShowSeatStatus;
import com.vivekvarma1.moviebooking.theatre.entity.SeatCategory;

import java.math.BigDecimal;

public record ShowSeatResponse(

        Long showSeatId,

        Long seatId,

//        String row,

        Integer seatNumber,

        String seatLabel,

        SeatCategory seatCategory,

        BigDecimal price,

        ShowSeatStatus status

) {}