package com.vivekvarma1.moviebooking.user.controller;

import com.vivekvarma1.moviebooking.user.entity.User;
import com.vivekvarma1.moviebooking.user.request.SendOtpRequest;
import com.vivekvarma1.moviebooking.user.request.VerifyOtpRequest;
import com.vivekvarma1.moviebooking.user.response.AuthResponse;
import com.vivekvarma1.moviebooking.user.response.MessageResponse;
import com.vivekvarma1.moviebooking.user.response.UserResponse;
import com.vivekvarma1.moviebooking.user.service.AuthenticationService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/send-otp")
    public ResponseEntity<MessageResponse> sendOtp(
            @Valid @RequestBody SendOtpRequest request
    ) {
        return ResponseEntity.ok(authenticationService.sendOtp(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authenticationService.verifyOtp(request, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletResponse response) {
        return ResponseEntity.ok(authenticationService.logout(response));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(authenticationService.me(user));
    }
}