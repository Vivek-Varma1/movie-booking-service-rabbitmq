package com.vivekvarma1.moviebooking.booking.entity;

import com.vivekvarma1.moviebooking.show.entity.ShowSeat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "booking_seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_showseat_once",
                        columnNames = {"show_seat_id"}
                )
        },
        indexes = {
                @Index(name = "idx_bookingseat_booking", columnList = "booking_id")
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

    BookingSeat(Booking booking, ShowSeat showSeat) {
        if (booking == null || showSeat == null) {
            throw new IllegalArgumentException("booking and showSeat are required");
        }
        this.booking = booking;
        this.showSeat = showSeat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingSeat that)) return false;
        return id != null && id.equals(that.id);
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