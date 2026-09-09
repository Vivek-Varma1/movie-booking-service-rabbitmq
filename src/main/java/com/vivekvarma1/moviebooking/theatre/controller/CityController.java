package com.vivekvarma1.moviebooking.theatre.controller;

import com.vivekvarma1.moviebooking.theatre.dto.request.CreateCitiesRequest;
import com.vivekvarma1.moviebooking.theatre.dto.request.CreateCityRequest;
import com.vivekvarma1.moviebooking.theatre.dto.response.CityResponse;
import com.vivekvarma1.moviebooking.theatre.service.CityServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityServiceImpl cityService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public List<CityResponse> createBulk(
            @RequestBody
            @Valid
            CreateCitiesRequest request
    ) {
        return cityService.createBulk(request);
    }
    @PutMapping("/{cityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public CityResponse update(
            @PathVariable Long cityId,
            @Valid @RequestBody CreateCityRequest request
    ) {
        return cityService.update(cityId, request);
    }

    @DeleteMapping("/{cityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long cityId) {
        cityService.delete(cityId);
        return ResponseEntity.noContent().build();
    }
}