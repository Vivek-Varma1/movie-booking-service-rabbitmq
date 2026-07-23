package com.vivekvarma1.moviebooking.show.entity;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.SeatAlreadyLockedException;
import com.vivekvarma1.moviebooking.theatre.entity.Seat;
import com.vivekvarma1.moviebooking.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "show_seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_show_seat",
                        columnNames = {
                                "show_id",
                                "seat_id"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_showseat_show",
                        columnList = "show_id"
                ),
                @Index(
                        name = "idx_showseat_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_showseat_show_id",
                        columnList = "show_id, show_seat_id"
                ),
                @Index(
                        name = "idx_showseat_status_lockeduntil",
                        columnList = "status, locked_until"
                ),
                @Index(
                        name = "idx_showseat_lockedby",
                        columnList = "locked_by_user_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShowSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_seat_id")
    private Long id;

    @Version
    private Long version;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "show_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_showseat_show")
    )
    private Show show;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "seat_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_showseat_seat")
    )
    private Seat seat;

    @NotNull
    @DecimalMin("0.00")
    @Column(
            name = "price",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private ShowSeatStatus status;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locked_by_user_id")
    private User lockedBy;

    public ShowSeat(
            Show show,
            Seat seat,
            BigDecimal price
    ) {
        this.show = show;
        this.seat = seat;
        this.price = price;
        this.status = ShowSeatStatus.AVAILABLE;
    }
//    public void lock(
//            User user,
//            LocalDateTime until
//    ) {
//        this.status = ShowSeatStatus.LOCKED;
//        this.lockedBy = user;
//        this.lockedUntil = until;
//    }
    public void lock(User user, LocalDateTime until) {
        if (!isAvailable()) {
            throw new SeatAlreadyLockedException(seat.getSeatLabel());
        }
        this.status = ShowSeatStatus.LOCKED;
        this.lockedBy = user;
        this.lockedUntil = until;
    }
/*
public void lock(User user, LocalDateTime until) {

    if (isLockExpired()) {
        unlock();
    }

    if (!isAvailable()) {
        throw new SeatAlreadyLockedException(seat.getSeatLabel());
    }

    this.status = ShowSeatStatus.LOCKED;
    this.lockedBy = user;
    this.lockedUntil = until;
}
 */
    public void book(User user) {
        if (!isLocked() || !this.lockedBy.equals(user)) {
            throw new IllegalStateException("Seat must be locked by the booking user before it can be booked");
        }
        this.status = ShowSeatStatus.BOOKED;
        this.lockedBy = null;
        this.lockedUntil = null;
    }
    public void unlock() {
        this.status = ShowSeatStatus.AVAILABLE;
        this.lockedBy = null;
        this.lockedUntil = null;
    }
    public boolean isLockExpired() {

        return status == ShowSeatStatus.LOCKED
                && lockedUntil != null
                && lockedUntil.isBefore(LocalDateTime.now());

    }
//    public void book() {
//        this.status = ShowSeatStatus.BOOKED;
//        this.lockedBy = null;
//        this.lockedUntil = null;
//    }
    public boolean isAvailable() {
        return status == ShowSeatStatus.AVAILABLE;
    }

    public boolean isLocked() {
        return status == ShowSeatStatus.LOCKED
                && lockedUntil != null
                && lockedUntil.isAfter(LocalDateTime.now());
    }

    public boolean isBooked() {
        return status == ShowSeatStatus.BOOKED;
    }
    public boolean isLockedBy(User user) {
        return lockedBy != null
                && lockedBy.getId().equals(user.getId())
                && isLocked();
    }
}