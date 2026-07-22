package com.vivekvarma1.moviebooking.show.mapper;

import com.vivekvarma1.moviebooking.show.dto.response.ShowSeatResponse;
import com.vivekvarma1.moviebooking.show.entity.ShowSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShowSeatMapper {

    @Mapping(target = "showSeatId", source = "id")
    @Mapping(target = "seatId", source = "seat.id")
//    @Mapping(target = "row", source = "seat.row")
    @Mapping(target = "seatNumber", source = "seat.seatNumber")
    @Mapping(
            target = "seatLabel",
            expression =
            "java(showSeat.getSeat().getRow()+showSeat.getSeat().getSeatNumber())"
    )
    @Mapping(target = "seatCategory", source = "seat.seatCategory")
    ShowSeatResponse toResponse(
            ShowSeat showSeat
    );
}