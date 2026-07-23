package com.vivekvarma1.moviebooking.booking.service;

import com.vivekvarma1.moviebooking.booking.dto.request.CreateBookingRequest;
import com.vivekvarma1.moviebooking.booking.dto.response.BookingResponse;
import com.vivekvarma1.moviebooking.booking.entity.Booking;
import com.vivekvarma1.moviebooking.booking.event.BookingConfirmedEvent;
import com.vivekvarma1.moviebooking.booking.mapper.BookingMapper;
import com.vivekvarma1.moviebooking.booking.repository.BookingRepository;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.InvalidShowSeatException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceNotFoundException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.SeatAlreadyLockedException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.SeatLockExpiredException;
import com.vivekvarma1.moviebooking.kafka.BookingEventProducer;
import com.vivekvarma1.moviebooking.show.entity.Show;
import com.vivekvarma1.moviebooking.show.entity.ShowSeat;
import com.vivekvarma1.moviebooking.show.repository.ShowRepository;
import com.vivekvarma1.moviebooking.show.repository.ShowSeatRepository;
import com.vivekvarma1.moviebooking.ticket.entity.Ticket;
import com.vivekvarma1.moviebooking.ticket.service.TicketService;
import com.vivekvarma1.moviebooking.user.entity.User;
import com.vivekvarma1.moviebooking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final TicketService ticketService;
    private final BookingEventProducer bookingEventProducer;

    @Override
    public BookingResponse createBooking(CreateBookingRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User"," id : ", request.userId()));

        Show show = showRepository.findById(request.showId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Show"," id : " , request.showId()));

        List<ShowSeat> showSeats =
                showSeatRepository.findAllByShowIdAndIdIn(
                        request.showId(),
                        request.showSeatIds()
                );

        if (showSeats.size() != request.showSeatIds().size()) {
            throw new InvalidShowSeatException(

            );
        }

        for (ShowSeat showSeat : showSeats) {
            if (showSeat.isLockExpired()) {
                showSeat.unlock();
                throw new SeatAlreadyLockedException(
                        "Seat lock has expired."
                );
            }

            if (!showSeat.isLockedBy(user)) {
                throw new SeatAlreadyLockedException(
                        showSeat.getSeat().getSeatLabel()
                );
            }

//            if (!showSeat.isLockedBy(user)) {
//                throw new IllegalStateException(
//                        "Seat "
//                                + showSeat.getSeat().getSeatLabel()
//                                + " is not locked by the user."
//                );
//            }

        }

        BigDecimal totalAmount = showSeats.stream()
                .map(ShowSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Booking booking = new Booking(
                show,
                user,
                totalAmount
        );

        for (ShowSeat showSeat : showSeats) {
            booking.addBookingSeat(showSeat);
        }

        booking.startPayment();

        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.toResponse(savedBooking);
    }

    @Override
    public BookingResponse confirmBooking(Long bookingId) {

        Booking booking = bookingRepository
                .findWithDetailsById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking", "id : " , bookingId));

        if (!booking.isPaymentStarted()) {
            throw new IllegalStateException(
                    "Booking is not awaiting payment confirmation."
            );
        }

        User user = booking.getUser();

        for (var bookingSeat : booking.getBookingSeats()) {

            ShowSeat showSeat = bookingSeat.getShowSeat();

//            if (!showSeat.isLockedBy(user)) {
//                throw new IllegalStateException(
//                        "Seat "
//                                + showSeat.getSeat().getSeatLabel()
//                                + " is no longer locked by the booking user."
//                );
//            }
            if (showSeat.isLockExpired()) {
                showSeat.unlock();
                throw new SeatLockExpiredException(
                        showSeat.getSeat().getSeatLabel()
                );
            }

            if (!showSeat.isLockedBy(user)) {
                throw new SeatAlreadyLockedException(
                        showSeat.getSeat().getSeatLabel()
                );
            }
            showSeat.book(user);
        }

        booking.confirm();
        Ticket ticket = ticketService.generateTicket(booking);
        bookingEventProducer.publish(

                new BookingConfirmedEvent(

                        booking.getId(),

                        ticket.getId(),

                        booking.getUser().getId(),

                        booking.getUser().getEmailAddress(),

                        booking.getUser().getName(),

                        ticket.getTicketNumber(),

                        booking.getShow()
                                .getMovie()
                                .getMovieName(),

                        booking.getShow()
                                .getScreen()
                                .getTheatre()
                                .getName(),

                        booking.getShow()
                                .getScreen()
                                .getName(),

                        booking.getShow()
                                .getStartDateTime(),

                        booking.getBookingSeats()
                                .stream()
                                .map(bs ->
                                        bs.getShowSeat()
                                                .getSeat()
                                                .getSeatLabel()
                                )
                                .toList()

                )

        );

        return bookingMapper.toResponse(booking);
    }

    @Override
    public BookingResponse cancelBooking(Long bookingId) {

        Booking booking = bookingRepository
                .findWithDetailsById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking"," id : " , bookingId));

        if (booking.isConfirmed()) {
            throw new IllegalStateException(
                    "Confirmed bookings cannot be cancelled."
            );
        }

        for (var bookingSeat : booking.getBookingSeats()) {

            ShowSeat showSeat = bookingSeat.getShowSeat();

            if (showSeat.isLocked()) {
                showSeat.unlock();
            }

        }

        booking.cancel();

        return bookingMapper.toResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBooking(Long bookingId) {

        Booking booking = bookingRepository
                .findWithDetailsById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking","id : " , bookingId));

        return bookingMapper.toResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getUserBookings(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User" ," id : " , userId));

        return bookingRepository
                .findByUserOrderByBookedAtDesc(user)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

}