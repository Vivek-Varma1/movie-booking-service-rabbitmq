package com.vivekvarma1.moviebooking.booking.entity;

import com.vivekvarma1.moviebooking.show.entity.ShowSeat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "booking_seats",
//        uniqueConstraints = {
//                @UniqueConstraint(
//                        name = "uk_showseat_once",
//                        columnNames = {"show_seat_id"}
//                )
//        },
        uniqueConstraints = {
                // Enforces that a show_seat_id can only have ONE row where active_flag = TRUE (1).
                // When active_flag is NULL (cancelled/expired), SQL allows unlimited rows.
                @UniqueConstraint(
                        name = "uk_showseat_active_booking",
                        columnNames = {"show_seat_id", "active_flag"}
                )
        },
        indexes = {
                @Index(name = "idx_bookingseat_booking", columnList = "booking_id"),
                @Index(name = "idx_bookingseat_showseat", columnList = "show_seat_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookingSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false, updatable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_seat_id", nullable = false, updatable = false)
    private ShowSeat showSeat;

    /**
     * Set to Boolean.TRUE for active/pending/confirmed bookings.
     * Set to NULL when the booking is CANCELLED or EXPIRED so the seat can be re-booked.
     */
    @Column(name = "active_flag")
    private Boolean activeFlag = Boolean.TRUE;

    BookingSeat(Booking booking, ShowSeat showSeat) {
        if (booking == null || showSeat == null) {
            throw new IllegalArgumentException("booking and showSeat are required");
        }
        this.booking = booking;
        this.showSeat = showSeat;
        this.activeFlag = Boolean.TRUE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingSeat that)) return false;
        return id != null && id.equals(that.id);
    }
    public void deactivate() {
        this.activeFlag = null; // Releasing the unique constraint
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "BookingSeat{id=%d}".formatted(id);
    }



}