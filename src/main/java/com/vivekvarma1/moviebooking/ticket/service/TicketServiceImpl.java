package com.vivekvarma1.moviebooking.ticket.service;

import com.vivekvarma1.moviebooking.booking.entity.Booking;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceNotFoundException;
import com.vivekvarma1.moviebooking.ticket.entity.Ticket;
import com.vivekvarma1.moviebooking.ticket.entity.TicketVerifyResponse;
import com.vivekvarma1.moviebooking.ticket.repository.TicketRepository;
import com.vivekvarma1.moviebooking.ticket.response.TicketResponse;
import com.vivekvarma1.moviebooking.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.InvalidBookingStateException;
import com.vivekvarma1.moviebooking.show.entity.Show;
//import com.vivekvarma1.moviebooking.ticket.response.TicketVerifyResponse;
import org.springframework.beans.factory.annotation.Value;
import java.time.ZoneId;
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
    public TicketResponse getTicket(Long bookingId, Long user) {

        Ticket ticket = ticketRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket",
                                "bookingId",
                                bookingId
                        ));
        if (!ticket.getBooking().getUser().getId().equals(user)) {
            throw new AccessDeniedException("You are not allowed to access this ticket.");
        }

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

                "https://movie-booking-service-rabbitmq.onrender.com/api/tickets/"
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
////    }
//private String buildQrContent(Ticket ticket) {
//
//    return "https://cineverse-backend.onrender.com/api/tickets/verify/"
//            + ticket.getTicketNumber();
//
//}
private String buildQrContent(Ticket ticket) {
    return frontendUrl + "/ticket/verify/" + ticket.getTicketNumber();
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
    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${ticket.check-in-opens-minutes-before:120}")
    private long checkInOpensMinutesBefore;

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    @Override
    @Transactional(readOnly = true)
    public TicketVerifyResponse verify(String ticketNumber) {
        return toVerifyResponse(findTicket(ticketNumber));
    }

    @Override
    public TicketVerifyResponse checkIn(String ticketNumber) {
        Ticket ticket = findTicket(ticketNumber);
        TicketVerifyResponse current = toVerifyResponse(ticket);

        if ("USED".equals(current.status())) {
            throw new InvalidBookingStateException("Ticket already used at " + current.usedAt() + ".");
        }
        if (!"VALID".equals(current.status())) {
            throw new InvalidBookingStateException("Ticket is " + current.status() + " and cannot be checked in.");
        }

        LocalDateTime now = LocalDateTime.now(IST);
        LocalDateTime opens = ticket.getBooking().getShow().getStartDateTime()
                .minusMinutes(checkInOpensMinutesBefore);
        if (now.isBefore(opens)) {
            throw new InvalidBookingStateException("Check-in opens " + checkInOpensMinutesBefore + " minutes before the show.");
        }

        if (ticketRepository.markUsed(current.ticketNumber(), now) == 0) {
            throw new InvalidBookingStateException("Ticket was just used by another scan.");
        }
        return toVerifyResponse(findTicket(current.ticketNumber()));
    }

    private Ticket findTicket(String ticketNumber) {
        String n = ticketNumber == null ? "" : ticketNumber.trim().toUpperCase();
        return ticketRepository.findByTicketNumber(n)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "ticketNumber", n));
    }

    private TicketVerifyResponse toVerifyResponse(Ticket t) {
        Booking b = t.getBooking();
        Show show = b.getShow();
        LocalDateTime now = LocalDateTime.now(IST);

        String status;
        if (t.getUsedAt() != null)                    status = "USED";
        else if (!b.isConfirmed())                    status = "INVALID";
        else if (show.getEndDateTime().isBefore(now)) status = "EXPIRED";
        else                                          status = "VALID";

        return new TicketVerifyResponse(
                t.getTicketNumber(),
                status,
                show.getMovie().getMovieName(),
                show.getScreen().getTheatre().getName(),
                show.getScreen().getName(),
                show.getStartDateTime(),
                b.getBookingSeats().stream()
                        .map(bs -> bs.getShowSeat().getSeat().getSeatLabel())
                        .toList(),
                t.getUsedAt());
    }
}
