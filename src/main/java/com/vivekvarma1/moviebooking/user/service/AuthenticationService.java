package com.vivekvarma1.moviebooking.user.service;

import com.vivekvarma1.moviebooking.user.entity.User;
import com.vivekvarma1.moviebooking.user.request.SendOtpRequest;
import com.vivekvarma1.moviebooking.user.request.VerifyOtpRequest;
import com.vivekvarma1.moviebooking.user.response.AuthResponse;
import com.vivekvarma1.moviebooking.user.response.MessageResponse;
import com.vivekvarma1.moviebooking.user.response.UserResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    MessageResponse sendOtp(SendOtpRequest request);

    AuthResponse verifyOtp(VerifyOtpRequest request, HttpServletResponse response);

    MessageResponse logout(HttpServletResponse response);

    UserResponse me(User user);
}