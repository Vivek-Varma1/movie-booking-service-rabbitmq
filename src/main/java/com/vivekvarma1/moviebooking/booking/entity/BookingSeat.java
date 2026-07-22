package com.vivekvarma1.moviebooking.booking.entity;

import com.vivekvarma1.moviebooking.show.entity.ShowSeat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "booking_seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_booking_showseat",
                        columnNames = {
                                "booking_id",
                                "show_seat_id"
                        }
                )
        }
)
@Getter
@NoArgsConstructor
public class BookingSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_seat_id")
    private ShowSeat showSeat;

    public BookingSeat(
            Booking booking,
            ShowSeat showSeat
    ) {

        this.booking = booking;
        this.showSeat = showSeat;

    }

}