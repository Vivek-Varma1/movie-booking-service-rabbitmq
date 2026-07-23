package com.vivekvarma1.moviebooking.booking.mapper;

import com.vivekvarma1.moviebooking.booking.dto.response.BookingResponse;
import com.vivekvarma1.moviebooking.booking.dto.response.BookingSeatResponse;
import com.vivekvarma1.moviebooking.booking.entity.Booking;
import com.vivekvarma1.moviebooking.booking.entity.BookingSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "bookingId", source = "id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "showId", source = "show.id")
    @Mapping(target = "movieName", source = "show.movie.movieName")
    @Mapping(target = "theatreName", source = "show.screen.theatre.name")
    @Mapping(target = "screenName", source = "show.screen.name")
    @Mapping(target = "bookingStatus", source = "bookingStatus")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "bookedAt", source = "bookedAt")
    @Mapping(target = "seats", source = "bookingSeats")
    BookingResponse toResponse(Booking booking);

    @Mapping(target = "showSeatId", source = "showSeat.id")
    @Mapping(target = "seatLabel", source = "showSeat.seat.seatLabel")
    @Mapping(target = "price", source = "showSeat.price")
    BookingSeatResponse toSeatResponse(BookingSeat bookingSeat);

}