package com.vivekvarma1.moviebooking.show.mapper;

import com.vivekvarma1.moviebooking.show.dto.response.ShowResponse;
import com.vivekvarma1.moviebooking.show.entity.Show;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShowMapper {

    @Mapping(target = "showId", source = "id")
    @Mapping(
            target = "startTime",
            expression = "java(show.getStartDateTime())"
    )
    @Mapping(
            target = "endTime",
            expression = "java(show.getEndDateTime())"
    )
    @Mapping(
            target = "durationInMinutes",
            expression = "java(show.getDurationInMinutes())"
    )
    @Mapping(
            target = "screenId",
            source = "screen.id"
    )
    @Mapping(
            target = "screenName",
            source = "screen.name"
    )
    @Mapping(
            target = "availableSeats",
            expression = "java(show.getAvailableSeats())"
    )
    @Mapping(
            target = "totalSeats",
            expression = "java(show.getTotalSeats())"
    )
    @Mapping(
            target = "minimumPrice",
            expression = "java(show.getMinimumPrice())"
    )
    @Mapping(
            target = "showSlot",
            source = "showSlot"
    )
    ShowResponse toResponse(Show show);
}