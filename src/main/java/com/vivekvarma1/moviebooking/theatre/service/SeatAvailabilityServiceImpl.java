package com.vivekvarma1.moviebooking.theatre.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatAvailabilityServiceImpl
        implements SeatAvailabilityService {

    @Override
    public List<Integer> getAvailableSeats(int showId) {
        return List.of();
    }
}