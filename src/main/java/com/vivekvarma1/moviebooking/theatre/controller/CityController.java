package com.vivekvarma1.moviebooking.theatre.controller;

import com.vivekvarma1.moviebooking.theatre.dto.request.CreateCitiesRequest;
import com.vivekvarma1.moviebooking.theatre.dto.request.CreateCityRequest;
import com.vivekvarma1.moviebooking.theatre.dto.response.CityResponse;
import com.vivekvarma1.moviebooking.theatre.service.CityServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityServiceImpl cityService;

    @PostMapping
    public CityResponse create(
            @RequestBody
            @Valid
            CreateCityRequest request
    ) {
        return cityService.create(request);
    }

    @GetMapping
    public List<CityResponse> getAll() {
        return cityService.getAll();
    }

    @PostMapping("/bulk")
    public List<CityResponse> createBulk(
            @RequestBody
            @Valid
            CreateCitiesRequest request
    ) {
        return cityService.createBulk(request);
    }
}