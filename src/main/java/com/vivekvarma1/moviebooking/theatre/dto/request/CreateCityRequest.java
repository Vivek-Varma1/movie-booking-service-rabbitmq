package com.vivekvarma1.moviebooking.theatre.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCityRequest(
        @NotBlank
        String name
) {}