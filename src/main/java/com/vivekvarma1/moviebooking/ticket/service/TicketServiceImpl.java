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
    private final QRCodeService qrCodeService;


    @Override
    public Ticket generateTicket(Booking booking) {

        Ticket ticket = Ticket.builder()
                .booking(booking)
                .ticketNumber(generateTicketNumber())
                .issuedAt(LocalDateTime.now())
                .build();
        String qrContent = buildQrContent(ticket);

        byte[] qrCode = qrCodeService.generateQRCode(qrContent);

        ticket.setQrCode(qrCode);
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

                "http://localhost:8080/api/tickets/"
                        + ticket.getId()
                        + "/qr"

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
//    private String buildQrContent(Ticket ticket, Booking booking) {
//
//        String seats = booking.getBookingSeats()
//                .stream()
//                .map(bs -> bs.getShowSeat()
//                        .getSeat()
//                        .getSeatLabel())
//                .toList()
//                .toString();
//
//        return """
//            Ticket Number : %s
//            Booking Id    : %d
//            Movie         : %s
//            Theatre       : %s
//            Screen        : %s
//            Show Time     : %s
//            Seats         : %s
//            """
//                .formatted(
//                        ticket.getTicketNumber(),
//                        booking.getId(),
//                        booking.getShow().getMovie().getMovieName(),
//                        booking.getShow().getScreen().getTheatre().getName(),
//                        booking.getShow().getScreen().getName(),
//                        booking.getShow().getStartDateTime(),
//                        seats
//                );
//    }
private String buildQrContent(Ticket ticket) {

    return "http://localhost:8080/api/tickets/verify/"
            + ticket.getTicketNumber();

}
    @Override
    @Transactional(readOnly = true)
    public byte[] getQrCode(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket",
                                "ticketId",
                                ticketId
                        ));

        return ticket.getQrCode();
    }

}