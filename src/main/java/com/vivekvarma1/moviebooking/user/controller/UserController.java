package com.vivekvarma1.moviebooking.user.controller;

import com.vivekvarma1.moviebooking.user.request.CreateUserRequest;
import com.vivekvarma1.moviebooking.user.response.UserResponse;
import com.vivekvarma1.moviebooking.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        userService.createUser(request)
                );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                userService.getUser(userId)
        );
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

}