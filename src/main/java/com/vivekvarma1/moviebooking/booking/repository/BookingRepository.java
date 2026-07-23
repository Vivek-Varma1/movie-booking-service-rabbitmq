package com.vivekvarma1.moviebooking.booking.repository;

import com.vivekvarma1.moviebooking.booking.entity.Booking;
import com.vivekvarma1.moviebooking.booking.entity.BookingStatus;
import com.vivekvarma1.moviebooking.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {
            "show",
            "show.movie",
            "show.screen",
            "show.screen.theatre",
            "bookingSeats",
            "bookingSeats.showSeat",
            "bookingSeats.showSeat.seat"
    })
    Optional<Booking> findWithDetailsById(Long bookingId);

    @EntityGraph(attributePaths = {
            "show",
            "show.movie",
            "show.screen",
            "show.screen.theatre"
    })
    List<Booking> findByUserOrderByBookedAtDesc(User user);

    @Query("""
       SELECT DISTINCT b
       FROM Booking b
       JOIN FETCH b.bookingSeats bs
       JOIN FETCH bs.showSeat ss
       WHERE b.bookingStatus = :status
       """)
    List<Booking> findByBookingStatus(
            BookingStatus status
    );

    List<Booking> findByBookingStatusAndBookedAtBefore(
            BookingStatus status,
            LocalDateTime time
    );

}