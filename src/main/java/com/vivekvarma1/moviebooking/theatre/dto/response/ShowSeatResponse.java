package com.vivekvarma1.moviebooking.theatre.dto.response;

import com.vivekvarma1.moviebooking.show.entity.ShowSeatStatus;
import com.vivekvarma1.moviebooking.theatre.entity.SeatCategory;

import java.math.BigDecimal;

public record ShowSeatResponse(
        Long seatId,
        String seatLabel,
        SeatCategory seatCategory,
        BigDecimal price,
        ShowSeatStatus status
) {}