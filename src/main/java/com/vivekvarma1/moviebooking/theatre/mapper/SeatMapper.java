package com.vivekvarma1.moviebooking.theatre.mapper;

import com.vivekvarma1.moviebooking.theatre.dto.response.SeatResponse;
import com.vivekvarma1.moviebooking.theatre.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(target = "seatId", source = "id")
    @Mapping(
            target = "seatLabel",
            expression = "java(seat.getSeatLabel())"
    )
    SeatResponse toResponse(Seat seat);
}