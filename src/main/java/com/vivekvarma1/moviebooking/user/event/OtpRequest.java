package com.vivekvarma1.moviebooking.user.event;

public record OtpRequest(
        String email,
        String otpCode
) {}