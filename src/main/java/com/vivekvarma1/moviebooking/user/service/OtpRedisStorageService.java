package com.vivekvarma1.moviebooking.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpRedisStorageService {

    private final StringRedisTemplate redisTemplate;
    private static final SecureRandom RANDOM = new SecureRandom();
    
    // Key prefix to organize Redis keys
    private static final String KEY_PREFIX = "OTP:"; 
    
    // TTL set to 2 minutes
    private static final Duration OTP_TTL = Duration.ofMinutes(2); 

    public String generateAndSaveOtp(String email) {
        String otp = String.format("%06d", RANDOM.nextInt(1000000));
        String redisKey = KEY_PREFIX + email;

        // Save to Redis with a 2-minute Expiration
        redisTemplate.opsForValue().set(redisKey, otp, OTP_TTL);

        return otp;
    }

    public boolean validateOtp(String email, String userInputCode) {
        String redisKey = KEY_PREFIX + email;
        String storedOtp = redisTemplate.opsForValue().get(redisKey);

        if (storedOtp == null) {
            return false; // OTP expired or never requested
        }

        if (storedOtp.equals(userInputCode)) {
            // Delete OTP from Redis immediately after successful single use
            redisTemplate.delete(redisKey); 
            return true;
        }

        return false;
    }
}