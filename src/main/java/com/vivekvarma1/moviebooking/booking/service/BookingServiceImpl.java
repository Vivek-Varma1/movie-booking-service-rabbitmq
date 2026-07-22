package com.vivekvarma1.moviebooking.booking.service;

import com.vivekvarma1.moviebooking.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    @Override
    public String createBooking(User user, long showId, List<Integer> seatIds) {
        return "";
    }
}