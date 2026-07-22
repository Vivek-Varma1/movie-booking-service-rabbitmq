package com.vivekvarma1.moviebooking.show.entity;

import com.vivekvarma1.moviebooking.event.entity.Movie;
import com.vivekvarma1.moviebooking.theatre.entity.Screen;
import com.vivekvarma1.moviebooking.theatre.entity.Seat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "shows",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_screen_date_slot",
                        columnNames = {
                                "screen_id",
                                "show_date",
                                "show_slot"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_show_movie",
                        columnList = "movie_id"
                ),
                @Index(
                        name = "idx_show_screen",
                        columnList = "screen_id"
                ),
                @Index(
                        name = "idx_show_date",
                        columnList = "show_date"
                )
        }
)
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_id")
    private Long id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "movie_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_show_movie")
    )
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "screen_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_show_screen")
    )
    private Screen screen;

    @Column(
            name = "show_date",
            nullable = false
    )
    private LocalDate showDate;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "show_slot",
            nullable = false,
            length = 20
    )
    private ShowSlot showSlot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShowStatus status;

    @OneToMany(
            mappedBy = "show",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<ShowSeat> showSeats = new ArrayList<>();

    public void addShowSeat(
            Seat seat,
            BigDecimal price
    ) {
        this.showSeats.add(
                new ShowSeat(
                        this,
                        seat,
                        price
                )
        );
    }

    public Show(
            Movie movie,
            Screen screen,
            LocalDate showDate,
            ShowSlot showSlot,
            ShowStatus status
    ) {
        this.movie = movie;
        this.screen = screen;
        this.showDate = showDate;
        this.showSlot = showSlot;
        this.status = status;
    }

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(
                showDate,
                showSlot.getStartTime()
        );
    }

    public LocalDateTime getEndDateTime() {
        return getStartDateTime()
                .plusMinutes(movie.getDurationInMinutes());
    }

    public Integer getAvailableSeats() {
        return (int) showSeats.stream()
                .filter(ShowSeat::isAvailable)
                .count();
    }

    public Integer getTotalSeats() {
        return showSeats.size();
    }

    public BigDecimal getMinimumPrice() {
        return showSeats.stream()
                .map(ShowSeat::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    public Integer getDurationInMinutes() {
        return movie.getDurationInMinutes();
    }

}