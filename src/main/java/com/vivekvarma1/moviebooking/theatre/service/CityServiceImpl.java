package com.vivekvarma1.moviebooking.theatre.service;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceAlreadyExistsException;
import com.vivekvarma1.moviebooking.theatre.dto.request.CreateCitiesRequest;
import com.vivekvarma1.moviebooking.theatre.dto.request.CreateCityRequest;
import com.vivekvarma1.moviebooking.theatre.dto.response.CityResponse;
import com.vivekvarma1.moviebooking.theatre.entity.City;
import com.vivekvarma1.moviebooking.theatre.mapper.CityMapper;
import com.vivekvarma1.moviebooking.theatre.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CityServiceImpl
        implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public CityResponse create(
            CreateCityRequest request
    ) {

        if (cityRepository.existsByNameIgnoreCase(
                request.name()
        )) {
            throw new ResourceAlreadyExistsException(
                    "City already exists."
            );
        }

        City city = new City(
                request.name().trim()
        );

        return cityMapper.toResponse(
                cityRepository.save(city)
        );
    }

    @Override
    public List<CityResponse> createBulk(
            CreateCitiesRequest request
    ) {

        List<City> citiesToSave =
                request.cities()
                        .stream()
                        .map(String::trim)
                        .filter(
                                city ->
                                        !cityRepository
                                                .existsByNameIgnoreCase(
                                                        city
                                                )
                        )
                        .map(City::new)
                        .toList();

        List<City> savedCities =
                cityRepository.saveAll(
                        citiesToSave
                );

        return cityMapper.toResponses(
                savedCities
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityResponse> getAll() {

        return cityMapper.toResponses(
                cityRepository.findAll()
        );
    }
}