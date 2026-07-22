package com.vivekvarma1.moviebooking.theatre.dto.request;

import com.vivekvarma1.moviebooking.theatre.entity.City;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

    public record CreateTheatreRequest(

            @NotBlank(message = "Name is required")
            @Size(max = 100)
            String name,

            @NotBlank(message = "Address is required")
            @Size(max = 255)
            String address,

            @NotNull
            Long cityId

    ) {}