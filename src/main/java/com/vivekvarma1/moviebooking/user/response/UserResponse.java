package com.vivekvarma1.moviebooking.user.response;

import com.vivekvarma1.moviebooking.user.entity.Role;
import lombok.Builder;

@Builder
public record UserResponse(

        Long id,

        String name,

        String email,

        String phoneNumber,

        Role role

) {
}