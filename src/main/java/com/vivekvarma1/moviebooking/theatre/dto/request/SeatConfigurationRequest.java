package com.vivekvarma1.moviebooking.theatre.dto.request;

import com.vivekvarma1.moviebooking.theatre.entity.SeatCategory;

public record SeatConfigurationRequest(
        String startRow,
        String endRow,
        Integer seatsPerRow,
        SeatCategory seatCategory
) {}