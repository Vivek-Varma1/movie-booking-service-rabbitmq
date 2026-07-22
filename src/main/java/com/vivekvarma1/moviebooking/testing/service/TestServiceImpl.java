package com.vivekvarma1.moviebooking.testing.service;

import com.vivekvarma1.moviebooking.show.repository.ShowSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TestServiceImpl implements TestService {

    private final ShowSeatRepository repository;

    @Override
    public void resetAllSeats() {

        repository.unlockAllSeats();

    }

    @Override
    public void resetSeat(Long seatId) {

        repository.unlockSeat(seatId);

    }

}