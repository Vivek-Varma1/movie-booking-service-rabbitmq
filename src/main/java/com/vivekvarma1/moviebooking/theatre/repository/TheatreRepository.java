package com.vivekvarma1.moviebooking.theatre.repository;

import com.vivekvarma1.moviebooking.theatre.entity.City;
import com.vivekvarma1.moviebooking.theatre.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
//    boolean existsByNameIgnoreCaseAndCityIgnoreCase(
//            String name,
//            City city
//    );
boolean existsByNameIgnoreCaseAndCityId(
        String name,
        Long cityId
);

    List<Theatre> findByCityIgnoreCase(
            City city
    );
}