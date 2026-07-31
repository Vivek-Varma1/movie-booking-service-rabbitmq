package com.vivekvarma1.moviebooking.booking.service;

import com.vivekvarma1.moviebooking.booking.dto.request.CreateBookingRequest;
import com.vivekvarma1.moviebooking.booking.dto.response.BookingResponse;
import com.vivekvarma1.moviebooking.user.entity.User;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(User user, CreateBookingRequest request);

    BookingResponse confirmBooking(Long userId,Long bookingId);

    BookingResponse cancelBooking(Long bookingId);

    BookingResponse getBooking(Long bookingId);

    List<BookingResponse> getUserBookings(Long userId);

}