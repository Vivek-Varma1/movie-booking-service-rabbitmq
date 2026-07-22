package com.vivekvarma1.moviebooking.user.response;

public record UserResponse(
        Long id,
        String name,
        String emailAddress
) {
}