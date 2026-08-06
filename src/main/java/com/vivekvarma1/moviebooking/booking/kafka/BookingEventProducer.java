package com.vivekvarma1.moviebooking.booking.kafka;

import com.vivekvarma1.moviebooking.booking.event.BookingConfirmedEvent;
import com.vivekvarma1.moviebooking.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publish(BookingConfirmedEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );

    }

}