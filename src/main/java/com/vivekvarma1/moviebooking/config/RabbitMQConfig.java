package com.vivekvarma1.moviebooking.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "booking.exchange";

    public static final String ROUTING_KEY = "booking.confirmed";

    @Bean
    TopicExchange bookingExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    MessageConverter jsonConverter() {
        return new JacksonJsonMessageConverter();
    }
}