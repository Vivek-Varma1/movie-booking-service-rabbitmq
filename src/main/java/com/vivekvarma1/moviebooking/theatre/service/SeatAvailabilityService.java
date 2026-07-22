package com.vivekvarma1.moviebooking.theatre.service;

import org.springframework.stereotype.Service;

import java.util.List;


public interface SeatAvailabilityService {

    List<Integer> getAvailableSeats(int showId);
}