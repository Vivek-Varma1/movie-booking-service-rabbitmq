package com.vivekvarma1.moviebooking.user.event;

public record OtpEvent(
        String email,
        String otpCode
) {}