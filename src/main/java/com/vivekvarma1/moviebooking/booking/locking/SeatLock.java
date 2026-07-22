package com.vivekvarma1.moviebooking.booking.locking;

import com.vivekvarma1.moviebooking.theatre.entity.Seat;
import com.vivekvarma1.moviebooking.show.entity.Show;
import com.vivekvarma1.moviebooking.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "seat_locks")
@Getter
@Setter
@NoArgsConstructor
public class SeatLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id")
    private Show show;

    @Column(name = "timeout_in_seconds")
    private Integer timeoutInSeconds;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lock_time")
    private Date lockTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locked_by")
    private User lockedBy;

    public SeatLock(Seat seat, Show show, Integer timeoutInSeconds, Date lockTime, User lockedBy) {
        this.seat = seat;
        this.show = show;
        this.timeoutInSeconds = timeoutInSeconds;
        this.lockTime = lockTime;
        this.lockedBy = lockedBy;
    }

    public boolean isLockExpired() {
        if (lockTime == null || timeoutInSeconds == null) {
            return true;
        }
        long expiryMillis = lockTime.getTime() + (timeoutInSeconds * 1000L);
        return System.currentTimeMillis() > expiryMillis;
    }
}