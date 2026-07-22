package com.vivekvarma1.moviebooking.theatre.service;


import com.vivekvarma1.moviebooking.theatre.dto.request.CreateTheatreRequest;
import com.vivekvarma1.moviebooking.theatre.dto.response.TheatreResponse;

public interface TheatreService {

    TheatreResponse createTheatre(
            CreateTheatreRequest request
    );

    TheatreResponse getTheatre(
            Long theatreId
    );
}