package com.vivekvarma1.moviebooking.show.service;

import com.vivekvarma1.moviebooking.show.dto.response.ShowResponse;
import com.vivekvarma1.moviebooking.show.dto.request.CreateShowRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ShowService {

    ShowResponse createShow(CreateShowRequest request);

    ShowResponse getShow(long showId);

    List<ShowResponse> getAllShows();

    void deleteShow(Long showId);
}