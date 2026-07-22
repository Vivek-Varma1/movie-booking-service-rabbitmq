
package com.vivekvarma1.moviebooking.theatre.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;

@Entity
@Table(
        name = "seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_screen_seat_row_number",
                        columnNames = {"screen_id", "seat_row", "seat_number"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_seat_screen",
                        columnList = "screen_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @NotBlank
    @Column(name = "seat_row", nullable = false, length = 2)
    private String row;

    @NotNull
    @Min(1)
    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_category", nullable = false, length = 20)
    private SeatCategory seatCategory;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "screen_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_seat_screen")
    )
    @Setter(AccessLevel.PACKAGE)
    private Screen screen;

    public Seat(
            String row,
            Integer seatNumber,
            SeatCategory seatCategory
    ) {
        this.row = Objects.requireNonNull(row).trim().toUpperCase();
        this.seatNumber = seatNumber;
        this.seatCategory = seatCategory;
    }

    public String getSeatLabel() {
        return row + seatNumber;
    }

    void assignScreen(Screen screen) {
        this.screen = screen;
    }
}