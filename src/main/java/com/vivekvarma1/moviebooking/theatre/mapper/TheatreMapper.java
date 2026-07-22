package com.vivekvarma1.moviebooking.theatre.mapper;

import com.vivekvarma1.moviebooking.theatre.dto.response.TheatreResponse;
import com.vivekvarma1.moviebooking.theatre.dto.response.TheatreSummaryResponse;
import com.vivekvarma1.moviebooking.theatre.entity.Theatre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(
        componentModel = "spring",
        uses = ScreenMapper.class
)
public interface TheatreMapper {

    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "cityName", source = "city.name")
    TheatreResponse toResponse(Theatre theatre);

    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "cityName", source = "city.name")
    TheatreSummaryResponse toSummary(Theatre theatre);
}