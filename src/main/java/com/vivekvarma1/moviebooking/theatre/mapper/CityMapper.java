package com.vivekvarma1.moviebooking.theatre.mapper;

import com.vivekvarma1.moviebooking.theatre.dto.response.CityResponse;
import com.vivekvarma1.moviebooking.theatre.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {

    @Mapping(
            target = "cityId",
            source = "id"
    )
    CityResponse toResponse(
            City city
    );

    List<CityResponse> toResponses(
            List<City> cities
    );
}