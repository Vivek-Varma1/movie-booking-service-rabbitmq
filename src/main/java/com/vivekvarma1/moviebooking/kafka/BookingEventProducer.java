package com.vivekvarma1.moviebooking.kafka;

import com.vivekvarma1.moviebooking.booking.event.BookingConfirmedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingEventProducer {

    private static final String TOPIC = "booking-confirmed";

    private final KafkaTemplate<String, BookingConfirmedEvent> kafkaTemplate;

    public void publish(BookingConfirmedEvent event) {

        kafkaTemplate.send(
                TOPIC,
                event.ticketNumber(),
                event
        );

    }

}