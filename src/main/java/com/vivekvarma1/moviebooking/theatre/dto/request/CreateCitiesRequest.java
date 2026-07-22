package com.vivekvarma1.moviebooking.theatre.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateCitiesRequest(

        @NotEmpty
        List<String> cities

) {
}