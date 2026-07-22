package com.vivekvarma1.moviebooking.user.repository;

import com.vivekvarma1.moviebooking.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<User, Long> {
    boolean existsByEmailAddress(String emailAddress);
}