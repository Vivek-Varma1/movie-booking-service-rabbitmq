package com.vivekvarma1.moviebooking.user.service;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceAlreadyExistsException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceNotFoundException;
import com.vivekvarma1.moviebooking.user.entity.User;
import com.vivekvarma1.moviebooking.user.mapper.UserMapper;
import com.vivekvarma1.moviebooking.user.repository.UserRepository;
import com.vivekvarma1.moviebooking.user.request.UpdateProfileRequest;
import com.vivekvarma1.moviebooking.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getUser(Long userId) {
        return userMapper.toResponse(
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User",
                                        "userId",
                                        userId
                                )
                        )
        );
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }
    @Override
    @Transactional
    public UserResponse updateProfile(User authenticatedUser, UpdateProfileRequest request) {
        // Check if phone number is already used by another account
        if (request.phoneNumber() != null &&
                !request.phoneNumber().equals(authenticatedUser.getPhoneNumber()) &&
                userRepository.existsByPhoneNumber(request.phoneNumber())) {

            throw new ResourceAlreadyExistsException("Phone number already in use: " + request.phoneNumber());
        }

        // Map request data to authenticated user entity
        userMapper.updateUserFromRequest(request, authenticatedUser);

        // Save updated user
        User updatedUser = userRepository.save(authenticatedUser);

        return userMapper.toResponse(updatedUser);
    }
}