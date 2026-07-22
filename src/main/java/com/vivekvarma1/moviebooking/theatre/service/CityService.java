package com.vivekvarma1.moviebooking.theatre.service;

import com.vivekvarma1.moviebooking.theatre.dto.request.CreateCitiesRequest;
import com.vivekvarma1.moviebooking.theatre.dto.request.CreateCityRequest;
import com.vivekvarma1.moviebooking.theatre.dto.response.CityResponse;

import java.util.List;

public interface CityService {

    CityResponse create(
            CreateCityRequest request
    );

    List<CityResponse> createBulk(
            CreateCitiesRequest request
    );

    List<CityResponse> getAll();
}