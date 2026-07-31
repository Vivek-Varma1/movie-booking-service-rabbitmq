package com.vivekvarma1.moviebooking.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @NotBlank(message = "Phone number cannot be blank")
        @Pattern(regexp = "^\\+?[1-9]\\d{9,14}$", message = "Invalid phone number format")
        String phoneNumber
) {}