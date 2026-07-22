package com.vivekvarma1.moviebooking.booking.repository;

import com.vivekvarma1.moviebooking.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository
        extends JpaRepository<Booking, Long> {

}