
package com.vivekvarma1.moviebooking.user.service;


import com.vivekvarma1.moviebooking.user.request.CreateUserRequest;
import com.vivekvarma1.moviebooking.user.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUser(Long userId);

    List<UserResponse> getAllUsers();

}