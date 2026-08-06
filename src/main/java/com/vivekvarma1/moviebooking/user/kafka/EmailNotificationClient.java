package com.vivekvarma1.moviebooking.user.kafka;

import com.vivekvarma1.moviebooking.user.event.OtpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class EmailNotificationClient {

    private final RestClient emailServiceRestClient;

    public void sendOtp(OtpRequest request) {

        emailServiceRestClient
                .post()
                .uri("/api/email/otp")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}