package com.vivekvarma1.moviebooking.user.service;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceAlreadyExistsException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceNotFoundException;

import com.vivekvarma1.moviebooking.user.entity.User;
import com.vivekvarma1.moviebooking.user.mapper.UserMapper;
import com.vivekvarma1.moviebooking.user.repository.UserRepository;
import com.vivekvarma1.moviebooking.user.request.CreateUserRequest;
import com.vivekvarma1.moviebooking.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmailAddress(request.emailAddress())) {
            throw new ResourceAlreadyExistsException(
                    "User already exists with email : "
                            + request.emailAddress()
            );
        }

        User savedUser = userRepository.save(
                userMapper.toEntity(request)
        );
//        User user = new User(
//                request.name(),
//                request.emailAddress()
//        );

       // User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

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

}