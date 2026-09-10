package com.vivekvarma1.moviebooking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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
    ObjectMapper rabbitObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    MessageConverter jsonConverter(ObjectMapper rabbitObjectMapper) {
        return new Jackson2JsonMessageConverter(rabbitObjectMapper);
    }
}