package com.vivekvarma1.moviebooking.theatre.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateScreenRequest(

        @NotBlank
        String name,

        @NotBlank
        String lastRow,

        @Min(1)
        Integer seatsPerRow,

        @NotEmpty
        @Valid
        List<SeatGenerationRuleRequest> seatRules

//        @NotEmpty
//        List<LocalTime> standardShowTimes
) {}