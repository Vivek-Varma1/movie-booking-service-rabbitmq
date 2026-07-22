package com.vivekvarma1.moviebooking.theatre.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(
        name = "screens",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_theatre_screen_name",
                        columnNames = {"theatre_id", "name"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_screen_theatre",
                        columnList = "theatre_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_id")
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Setter(AccessLevel.PACKAGE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "theatre_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_screen_theatre")
    )
    private Theatre theatre;

    @OneToMany(
            mappedBy = "screen",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<Seat> seats = new ArrayList<>();


//    @ElementCollection
//    @CollectionTable(
//            name = "screen_standard_slots",
//            joinColumns = @JoinColumn(
//                    name = "screen_id",
//                    foreignKey = @ForeignKey(name = "fk_slot_screen")
//            )
//    )
//   @Column(name = "slot_time", nullable = false)
//    private final List<LocalTime> standardShowTimes = new ArrayList<>();

    public Screen(String name) {
        this.name = name.trim();
    }

    public void addSeat(Seat seat) {
        if (seat == null) {
            throw new IllegalArgumentException("Seat cannot be null");
        }

        if (!seats.contains(seat)) {
            seats.add(seat);
            seat.setScreen(this);
        }
    }

    public void removeSeat(Seat seat) {
        if (seat == null) {
            return;
        }

        seats.remove(seat);
        seat.setScreen(null);
    }

//    public void assignShowSlots(List<LocalTime> slotTimes) {
//        this.standardShowTimes.clear();
//        for (LocalTime time : slotTimes) {
//            if (!this.standardShowTimes.contains(time)) {
//                this.standardShowTimes.add(time);
//            }
//        }
//    }
//
//    public boolean hasShowSlot(LocalTime slotTime) {
//        return standardShowTimes.contains(slotTime);
//    }
}