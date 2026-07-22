package com.vivekvarma1.moviebooking.booking.entity;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.InvalidBookingStateException;
import com.vivekvarma1.moviebooking.theatre.entity.Seat;
import com.vivekvarma1.moviebooking.show.entity.Show;
import com.vivekvarma1.moviebooking.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "bookings")
@Getter
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_id")
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDateTime bookedAt;

    @OneToMany(
            mappedBy = "booking",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BookingSeat> bookingSeats = new ArrayList<>();

    public Booking(
            Show show,
            User user,
            BigDecimal totalAmount
    ) {

        this.show = show;
        this.user = user;
        this.totalAmount = totalAmount;
        this.bookedAt = LocalDateTime.now();
        this.bookingStatus = BookingStatus.CREATED;

    }

    public void addBookingSeat(BookingSeat bookingSeat) {

        bookingSeats.add(bookingSeat);

    }

    public boolean isCreated() {

        return bookingStatus == BookingStatus.CREATED;

    }

    public boolean isConfirmed() {

        return bookingStatus == BookingStatus.CONFIRMED;

    }

    public void confirm() {

        if (!isCreated()) {

            throw new InvalidBookingStateException(
                    "Only CREATED bookings can be confirmed."
            );

        }

        bookingStatus = BookingStatus.CONFIRMED;

    }

    public void cancel() {

        if (!isCreated()) {

            throw new InvalidBookingStateException(
                    "Only CREATED bookings can be cancelled."
            );

        }

        bookingStatus = BookingStatus.CANCELLED;

    }

    public void expire() {

        if (!isCreated()) {

            throw new InvalidBookingStateException(
                    "Only CREATED bookings can expire."
            );

        }

        bookingStatus = BookingStatus.EXPIRED;

    }

}