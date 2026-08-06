package com.vivekvarma1.moviebooking.user.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
@Configuration
public class RestClientConfig {

    @Value("${email.service.url}")
    private String emailServiceUrl;

    @Bean
    public RestClient emailServiceRestClient() {
        return RestClient.builder()
                .baseUrl(emailServiceUrl)
                .build();
    }
}