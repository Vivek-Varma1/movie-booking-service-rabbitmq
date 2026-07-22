package com.vivekvarma1.moviebooking.theatre.service;

import com.vivekvarma1.moviebooking.theatre.dto.request.CreateScreenRequest;
import com.vivekvarma1.moviebooking.theatre.dto.response.ScreenResponse;
import com.vivekvarma1.moviebooking.theatre.dto.response.ScreenSummaryResponse;

import java.util.List;

public interface ScreenService {

    ScreenResponse createScreen(
            Long theatreId,
            CreateScreenRequest request
    );

    ScreenResponse getScreen(
            Long screenId
    );
    void deleteScreen(
            Long theatreId,
            Long screenId
    );
    List<ScreenSummaryResponse> getScreensByTheatre(
            Long theatreId
    );
}