package com.vivekvarma1.moviebooking.user.response;

public record AuthResponse(
        String token,
        UserResponse user
) {}