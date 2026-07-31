package com.vivekvarma1.moviebooking.user.kafka;
import com.vivekvarma1.moviebooking.user.event.OtpEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpKafkaProducer {

    private final KafkaTemplate<String, OtpEvent> kafkaTemplate;
    private static final String TOPIC = "user-otp-topic";

    public void sendOtpEvent(String email, String otpCode) {
        log.info("Publishing OTP event to Kafka topic '{}' for email: {}", TOPIC, email);
        kafkaTemplate.send(TOPIC, email, new OtpEvent(email, otpCode));
    }
}