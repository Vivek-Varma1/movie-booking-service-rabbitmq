package com.vivekvarma1.moviebooking.user.service;

import com.vivekvarma1.moviebooking.user.entity.User;
import com.vivekvarma1.moviebooking.user.request.UpdateProfileRequest;
import com.vivekvarma1.moviebooking.user.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse getUser(Long userId);

    List<UserResponse> getAllUsers();

    // Optional: Add profile update if user wants to set name/phone later
    // UserResponse updateProfile(Long userId, UpdateProfileRequest request);
    UserResponse updateProfile(User authenticatedUser, UpdateProfileRequest request);
}