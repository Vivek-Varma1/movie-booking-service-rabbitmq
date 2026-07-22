package com.vivekvarma1.moviebooking.booking.service;

import com.vivekvarma1.moviebooking.user.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BookingService {

    String createBooking(User user,
                         long showId, List<Integer> seatIds);
}