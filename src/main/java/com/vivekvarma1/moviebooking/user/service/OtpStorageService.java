package com.vivekvarma1.moviebooking.user.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpStorageService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final long EXPIRATION_MINUTES = 5;

    private record OtpEntry(String code, Instant expiresAt) {}

    private final ConcurrentHashMap<String, OtpEntry> otpCache = new ConcurrentHashMap<>();

    public String generateAndSaveOtp(String email) {
        String otp = String.format("%06d", RANDOM.nextInt(1000000));
        Instant expiresAt = Instant.now().plusSeconds(EXPIRATION_MINUTES * 60);
        otpCache.put(email, new OtpEntry(otp, expiresAt));
        return otp;
    }

    public boolean validateOtp(String email, String userInputCode) {
        OtpEntry entry = otpCache.get(email);
        if (entry == null) return false;

        if (Instant.now().isAfter(entry.expiresAt())) {
            otpCache.remove(email);
            return false;
        }

        if (entry.code().equals(userInputCode)) {
            otpCache.remove(email); // Invalidate token after single use
            return true;
        }

        return false;
    }
}