package com.vivekvarma1.moviebooking.user.kafka;

import com.vivekvarma1.moviebooking.user.event.OtpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpEmailClientService {

    private final EmailNotificationClient emailNotificationClient;

    public void sendOtp(String email, String otpCode) {

        log.info("Sending OTP to Email Notification Service");

        emailNotificationClient.sendOtp(
                new OtpRequest(email, otpCode)
        );
    }
}