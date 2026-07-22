package com.vivekvarma1.moviebooking.booking.repository;

import com.vivekvarma1.moviebooking.booking.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingSeatRepository
        extends JpaRepository<BookingSeat, Long> {

}