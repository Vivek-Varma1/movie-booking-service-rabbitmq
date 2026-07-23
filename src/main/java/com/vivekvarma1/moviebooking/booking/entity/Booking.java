package com.vivekvarma1.moviebooking.booking.entity;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.InvalidBookingStateException;
import com.vivekvarma1.moviebooking.show.entity.ShowSeat;
import com.vivekvarma1.moviebooking.theatre.entity.Seat;
import com.vivekvarma1.moviebooking.show.entity.Show;
import com.vivekvarma1.moviebooking.ticket.entity.Ticket;
import com.vivekvarma1.moviebooking.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Entity
@Table(
        name = "bookings",
        indexes = {
                @Index(name = "idx_booking_user", columnList = "user_id"),
                @Index(name = "idx_booking_status", columnList = "status"),
                @Index(name = "idx_booking_show", columnList = "show_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_id", nullable = false, updatable = false)
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BookingStatus bookingStatus;

    @Column(nullable = false, precision = 10, scale = 2, updatable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime bookedAt;

    @OneToMany(
            mappedBy = "booking",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<BookingSeat> bookingSeats = new ArrayList<>();
    @OneToOne(
            mappedBy = "booking",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Ticket ticket;

    public Booking(
            Show show,
            User user,
            BigDecimal totalAmount
    ) {
        if (show == null || user == null) {
            throw new IllegalArgumentException("show and user are required");
        }
        if (totalAmount == null || totalAmount.signum() < 0) {
            throw new IllegalArgumentException("totalAmount must be non-negative");
        }
        this.show = show;
        this.user = user;
        this.totalAmount = totalAmount;
        this.bookedAt = LocalDateTime.now();
        this.bookingStatus = BookingStatus.CREATED;

    }

//    public void addBookingSeat(BookingSeat bookingSeat) {
//
//        bookingSeats.add(bookingSeat);
//
//    }



//    public void confirm() {
//
//        if (!isCreated()) {
//
//            throw new InvalidBookingStateException(
//                    "Only CREATED bookings can be confirmed."
//            );
//
//        }
//
//        bookingStatus = BookingStatus.CONFIRMED;
//
//    }
//
//    public void cancel() {
//
//        if (!isCreated()) {
//
//            throw new InvalidBookingStateException(
//                    "Only CREATED bookings can be cancelled."
//            );
//
//        }
//
//        bookingStatus = BookingStatus.CANCELLED;
//
//    }
//
//    public void expire() {
//
//        if (!isCreated()) {
//
//            throw new InvalidBookingStateException(
//                    "Only CREATED bookings can expire."
//            );
//
//        }

//        bookingStatus = BookingStatus.EXPIRED;
//
//    }
    /**
     * Single entry point for attaching a seat to this booking. Builds the
     * BookingSeat and wires both sides of the association in one place, so
     * there's no way to end up with a BookingSeat pointing at this booking
     * that isn't also present in bookingSeats (or vice versa).
     */
    public void addBookingSeat(ShowSeat showSeat) {
        if (!isCreated()) {
            throw new InvalidBookingStateException(
                    "Seats can only be added while the booking is CREATED."
            );
        }
        BookingSeat bookingSeat = new BookingSeat(this, showSeat);
        bookingSeats.add(bookingSeat);
    }

    @PrePersist
    void onCreate() {
        if (bookedAt == null) {
            bookedAt = LocalDateTime.now();
        }
    }
    public List<BookingSeat> getBookingSeats() {
        return Collections.unmodifiableList(bookingSeats);
    }

    public boolean isCreated() {
        return bookingStatus == BookingStatus.CREATED;
    }

    public boolean isPaymentStarted() {
        return bookingStatus == BookingStatus.PAYMENT_STARTED;
    }

    public boolean isConfirmed() {
        return bookingStatus == BookingStatus.CONFIRMED;
    }

    /** CREATED -> PAYMENT_STARTED. Call when handing off to the payment provider. */
    public void startPayment() {
        if (!isCreated()) {
            throw new InvalidBookingStateException(
                    "Only CREATED bookings can start payment."
            );
        }
        bookingStatus = BookingStatus.PAYMENT_STARTED;
    }

    /** PAYMENT_STARTED -> CONFIRMED. Call on a successful payment webhook/callback. */
    public void confirm() {
        if (!isPaymentStarted()) {
            throw new InvalidBookingStateException(
                    "Only bookings with PAYMENT_STARTED can be confirmed."
            );
        }
        bookingStatus = BookingStatus.CONFIRMED;
    }

    /** CREATED or PAYMENT_STARTED -> CANCELLED. Confirmed bookings need a separate refund flow. */
    public void cancel() {
        if (!isCreated() && !isPaymentStarted()) {
            throw new InvalidBookingStateException(
                    "Only CREATED or PAYMENT_STARTED bookings can be cancelled."
            );
        }
        bookingStatus = BookingStatus.CANCELLED;
    }

    /** CREATED or PAYMENT_STARTED -> EXPIRED. Call from the seat-hold reaper job. */
    public void expire() {
        if (!isCreated() && !isPaymentStarted()) {
            throw new InvalidBookingStateException(
                    "Only CREATED or PAYMENT_STARTED bookings can expire."
            );
        }
        bookingStatus = BookingStatus.EXPIRED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking booking)) return false;
        return id != null && id.equals(booking.id);
    }

    @Override
    public int hashCode() {
        // Stable across the entity lifecycle (transient -> managed -> detached),
        // unlike hashing on id which changes once it's assigned.
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        // Deliberately excludes lazy associations (show, user, bookingSeats) to
        // avoid triggering LazyInitializationException outside a session.
        return "Booking{id=%d, status=%s, totalAmount=%s}"
                .formatted(id, bookingStatus, totalAmount);
    }

}