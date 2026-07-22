package com.vivekvarma1.moviebooking.theatre.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTheatreRequest(

        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Size(max = 255)
        String address,

        @NotBlank
        @Size(max = 100)
        String city

) {}