package com.vivekvarma1.moviebooking.booking.service;

import com.vivekvarma1.moviebooking.booking.dto.request.CreateBookingRequest;
import com.vivekvarma1.moviebooking.booking.dto.response.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(CreateBookingRequest request);

    BookingResponse confirmBooking(Long bookingId);

    BookingResponse cancelBooking(Long bookingId);

    BookingResponse getBooking(Long bookingId);

    List<BookingResponse> getUserBookings(Long userId);

}