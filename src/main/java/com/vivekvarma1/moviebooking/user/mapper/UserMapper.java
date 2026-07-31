package com.vivekvarma1.moviebooking.user.mapper;

import com.vivekvarma1.moviebooking.user.entity.User;
import com.vivekvarma1.moviebooking.user.request.UpdateProfileRequest;
import com.vivekvarma1.moviebooking.user.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    void updateUserFromRequest(UpdateProfileRequest request, @MappingTarget User user);
}