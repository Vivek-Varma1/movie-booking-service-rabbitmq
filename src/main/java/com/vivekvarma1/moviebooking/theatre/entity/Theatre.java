package com.vivekvarma1.moviebooking.theatre.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "theatres",
        indexes = {
                @Index(
                        name = "idx_theatre_city",
                        columnList = "city_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theatre_id")
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "city_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_theatre_city")
    )
    private City city;

//    @NotBlank
//    @Column(nullable = false, length = 100)
//    private String city;

    @OneToMany(
            mappedBy = "theatre",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<Screen> screens = new ArrayList<>();

    public Theatre(
            String name,
            String address,
            City city
    ) {
        this.name = name.trim();
        this.address = address.trim();
        this.city = city;
    }

    public void addScreen(Screen screen) {
        if (screen == null) {
            throw new IllegalArgumentException("Screen cannot be null");
        }

        if (!screens.contains(screen)) {
            screens.add(screen);
            screen.setTheatre(this);
        }
    }

    public void removeScreen(Screen screen) {
        if (screen == null) {
            return;
        }

        screens.remove(screen);
    }

    public void rename(String name) {
        this.name = name.trim();
    }

    public void changeAddress(String address) {
        this.address = address.trim();
    }

    public void changeCity(City city) {
        this.city = city;
    }
}