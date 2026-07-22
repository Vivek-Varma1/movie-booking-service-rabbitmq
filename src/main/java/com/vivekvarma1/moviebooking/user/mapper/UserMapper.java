package com.vivekvarma1.moviebooking.user.mapper;

import com.vivekvarma1.moviebooking.user.entity.User;
import com.vivekvarma1.moviebooking.user.request.CreateUserRequest;
import com.vivekvarma1.moviebooking.user.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

        @Mapping(target = "name", source = "name")
        @Mapping(target = "emailAddress", source = "emailAddress")
        @Mapping(target = "id", ignore = true)
        User toEntity(CreateUserRequest request);

        UserResponse toResponse(User user);


}