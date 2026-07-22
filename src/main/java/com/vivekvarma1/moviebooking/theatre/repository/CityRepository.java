package com.vivekvarma1.moviebooking.theatre.repository;

import com.vivekvarma1.moviebooking.theatre.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository
        extends JpaRepository<City, Long> {

    Optional<City> findByNameIgnoreCase(
            String name
    );

    boolean existsByNameIgnoreCase(
            String name
    );

    List<City> findByNameIn(
            List<String> names
    );
}