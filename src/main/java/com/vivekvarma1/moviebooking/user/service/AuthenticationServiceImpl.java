package com.vivekvarma1.moviebooking.user.service;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceNotFoundException;
import com.vivekvarma1.moviebooking.security.cookie.CookieService;
import com.vivekvarma1.moviebooking.security.jwt.JwtService;
import com.vivekvarma1.moviebooking.user.entity.User;
import com.vivekvarma1.moviebooking.user.kafka.OtpEmailClientService;
import com.vivekvarma1.moviebooking.user.mapper.UserMapper;
import com.vivekvarma1.moviebooking.user.repository.UserRepository;
import com.vivekvarma1.moviebooking.user.request.SendOtpRequest;
import com.vivekvarma1.moviebooking.user.request.VerifyOtpRequest;
import com.vivekvarma1.moviebooking.user.response.AuthResponse;
import com.vivekvarma1.moviebooking.user.response.MessageResponse;
import com.vivekvarma1.moviebooking.user.response.UserResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final OtpRedisStorageService otpRedisStorageService;
    private final OtpEmailClientService otpEmailClientService;
    private final JwtService jwtService;
    private final CookieService cookieService;

    @Override
    public MessageResponse sendOtp(SendOtpRequest request) {
        String email = request.email();
        String otp = otpRedisStorageService.generateAndSaveOtp(email);
        otpEmailClientService.sendOtp(email, otp);
        return new MessageResponse("OTP code sent successfully to " + email);
    }

    @Override
    public AuthResponse verifyOtp(VerifyOtpRequest request, HttpServletResponse response) {
        String email = request.email();

        boolean isValid = otpRedisStorageService.validateOtp(email, request.otp());
        if (!isValid) {
            throw new BadCredentialsException("Invalid or expired OTP code.");
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .build()
                ));

        String jwtToken = jwtService.generateToken(user);

        // Add JWT into HttpOnly Cookie
        cookieService.addAccessTokenCookie(response, jwtToken);

        return new AuthResponse(jwtToken, userMapper.toResponse(user));
    }

    @Override
    public MessageResponse logout(HttpServletResponse response) {
        cookieService.clearAccessTokenCookie(response);
        return new MessageResponse("Successfully logged out.");
    }

    @Override
    public UserResponse me(User user) {
        if (user == null) {
            throw new ResourceNotFoundException("User", "session", "current");
        }
        return userMapper.toResponse(user);
    }
}