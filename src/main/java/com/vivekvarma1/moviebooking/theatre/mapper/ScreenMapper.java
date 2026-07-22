package com.vivekvarma1.moviebooking.theatre.mapper;

import com.vivekvarma1.moviebooking.theatre.dto.response.ScreenResponse;
import com.vivekvarma1.moviebooking.theatre.dto.response.ScreenSummaryResponse;
import com.vivekvarma1.moviebooking.theatre.entity.Screen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = SeatMapper.class
)
public interface ScreenMapper {

    @Mapping(
            target = "totalSeats",
            expression = "java(screen.getSeats().size())"
    )

    ScreenResponse toResponse(Screen screen);

    @Mapping(
            target = "totalSeats",
            expression = "java(screen.getSeats().size())"
    )
    ScreenSummaryResponse toSummary(Screen screen);

    List<ScreenSummaryResponse> toSummaryResponses(
            List<Screen> screens
    );
}