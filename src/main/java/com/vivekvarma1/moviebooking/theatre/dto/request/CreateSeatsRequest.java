package com.vivekvarma1.moviebooking.theatre.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateSeatsRequest(

        @NotEmpty
        @Valid
        List<SeatGenerationRuleRequest> seatRules

) {}