package com.vivekvarma1.moviebooking.theatre.dto.request;

import com.vivekvarma1.moviebooking.theatre.entity.SeatCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SeatGenerationRuleRequest(

        @NotBlank
        String fromRow,

        @NotBlank
        String toRow,

        @NotNull
        SeatCategory seatCategory

) {}