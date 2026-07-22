package com.vivekvarma1.moviebooking.theatre.dto.response;

import com.vivekvarma1.moviebooking.theatre.entity.SeatCategory;

public record SeatResponse(
        Long seatId,
        String seatLabel,
        SeatCategory seatCategory
) {}