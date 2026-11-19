package com.vivekvarma1.moviebooking.booking.repository;

import com.vivekvarma1.moviebooking.booking.entity.Booking;
import com.vivekvarma1.moviebooking.booking.entity.BookingStatus;
import com.vivekvarma1.moviebooking.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> , JpaSpecificationExecutor<Booking> {

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

//    @Query("""
//       SELECT DISTINCT b
//       FROM Booking b
//       JOIN FETCH b.bookingSeats bs
//       JOIN FETCH bs.showSeat ss
//       WHERE b.bookingStatus = :status
//       """)
//    List<Booking> findByBookingStatus(
//            BookingStatus status
//    );
@Query("""
   SELECT DISTINCT b
   FROM Booking b
   LEFT JOIN FETCH b.bookingSeats bs
   LEFT JOIN FETCH bs.showSeat ss
   LEFT JOIN FETCH ss.seat
   LEFT JOIN FETCH b.user
   WHERE b.bookingStatus = :status
   """)
List<Booking> findByBookingStatus(@Param("status") BookingStatus status);

    List<Booking> findByBookingStatusAndBookedAtBefore(
            BookingStatus status,
            LocalDateTime time
    );


    long countByBookingStatus(BookingStatus status);

    @Query("select coalesce(sum(b.totalAmount), 0) from Booking b where b.bookingStatus = :status and b.bookedAt >= :from")
    BigDecimal sumRevenueSince(@Param("status") BookingStatus status, @Param("from") LocalDateTime from);

    @Query("select count(b) from Booking b where b.bookingStatus = :status and b.bookedAt >= :from")
    long countSince(@Param("status") BookingStatus status, @Param("from") LocalDateTime from);

    @Query("select count(bs) from BookingSeat bs where bs.booking.bookingStatus = :status and bs.booking.bookedAt >= :from")
    long countSeatsSince(@Param("status") BookingStatus status, @Param("from") LocalDateTime from);

    @Query("select b.bookedAt, b.totalAmount from Booking b where b.bookingStatus = :status and b.bookedAt >= :from")
    List<Object[]> amountsSince(@Param("status") BookingStatus status, @Param("from") LocalDateTime from);

    @Query("""
            select m.movieName, m.language, sum(b.totalAmount), count(b)
            from Booking b join b.show s join s.movie m
            where b.bookingStatus = :status and b.bookedAt >= :from
            group by m.id, m.movieName, m.language
            order by sum(b.totalAmount) desc
            """)
    List<Object[]> topMovies(@Param("status") BookingStatus status,
                             @Param("from") LocalDateTime from,
                             Pageable pageable);
}