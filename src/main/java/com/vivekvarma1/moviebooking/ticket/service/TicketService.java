package com.vivekvarma1.moviebooking.ticket.service;

import com.vivekvarma1.moviebooking.booking.entity.Booking;
import com.vivekvarma1.moviebooking.ticket.entity.Ticket;
import com.vivekvarma1.moviebooking.ticket.response.TicketResponse;

public interface TicketService {

    Ticket generateTicket(Booking booking);

    TicketResponse getTicket(Long bookingId);

    byte[] getQrCode(Long ticketId);

}