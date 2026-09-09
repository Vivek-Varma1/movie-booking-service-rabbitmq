package com.vivekvarma1.moviebooking.theatre.service;


import com.vivekvarma1.moviebooking.theatre.dto.request.CreateTheatreRequest;
import com.vivekvarma1.moviebooking.theatre.dto.request.UpdateTheatreRequest;
import com.vivekvarma1.moviebooking.theatre.dto.response.TheatreResponse;
import com.vivekvarma1.moviebooking.theatre.dto.response.TheatreSummaryResponse;

import java.util.List;

public interface TheatreService {

    TheatreResponse createTheatre(
            CreateTheatreRequest request
    );

    TheatreResponse getTheatre(
            Long theatreId
    );
    List<TheatreSummaryResponse> getAllTheatres();

    TheatreResponse updateTheatre(Long theatreId, UpdateTheatreRequest request);
    void deleteTheatre(Long theatreId);
}