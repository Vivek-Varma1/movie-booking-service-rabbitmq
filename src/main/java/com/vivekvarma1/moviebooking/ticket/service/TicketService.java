package com.vivekvarma1.moviebooking.ticket.service;

import com.vivekvarma1.moviebooking.booking.entity.Booking;
import com.vivekvarma1.moviebooking.ticket.entity.Ticket;
import com.vivekvarma1.moviebooking.ticket.response.TicketResponse;
import com.vivekvarma1.moviebooking.user.entity.User;

public interface TicketService {

    Ticket generateTicket(Booking booking);

    TicketResponse getTicket(Long bookingId, Long user);

    byte[] getQrCode(Long ticketId);

}