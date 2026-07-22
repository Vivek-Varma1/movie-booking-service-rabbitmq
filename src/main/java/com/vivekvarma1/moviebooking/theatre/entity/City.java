package com.vivekvarma1.moviebooking.theatre.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "cities",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_city_name",
            columnNames = "name"
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    public City(String name) {
        this.name = name.trim();
    }
}