package com.vivekvarma1.moviebooking.show.service;

import com.vivekvarma1.moviebooking.show.dto.request.LockSeatsRequest;
import com.vivekvarma1.moviebooking.show.dto.response.LockSeatsResponse;
import com.vivekvarma1.moviebooking.show.dto.response.ShowSeatLayoutResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ShowSeatService {

    ShowSeatLayoutResponse getSeatLayout(
            Long showId
    );
    LockSeatsResponse lockSeats(
            LockSeatsRequest request
    );


    List<Long> getShowSeatIds(Long showId);
}