package com.vivekvarma1.moviebooking.ticket.service;

import com.vivekvarma1.moviebooking.booking.entity.Booking;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceNotFoundException;
import com.vivekvarma1.moviebooking.ticket.entity.Ticket;
import com.vivekvarma1.moviebooking.ticket.repository.TicketRepository;
import com.vivekvarma1.moviebooking.ticket.response.TicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public Ticket generateTicket(Booking booking) {

        Ticket ticket = Ticket.builder()
                .booking(booking)
                .ticketNumber(generateTicketNumber())
                .issuedAt(LocalDateTime.now())
                .build();

        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse getTicket(Long bookingId) {

        Ticket ticket = ticketRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket",
                                "bookingId",
                                bookingId
                        ));

        Booking booking = ticket.getBooking();

        return new TicketResponse(

                ticket.getTicketNumber(),

                booking.getShow().getMovie().getMovieName(),

                booking.getShow().getScreen().getTheatre().getName(),

                booking.getShow().getScreen().getName(),

                booking.getShow().getStartDateTime(),

                booking.getBookingSeats()
                        .stream()
                        .map(bs -> bs.getShowSeat()
                                .getSeat()
                                .getSeatLabel())
                        .toList(),

                ticket.getIssuedAt(),

                null
        );
    }

    private String generateTicketNumber() {

        return "TKT-"
                + LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE)
                + "-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

}