package com.vivekvarma1.moviebooking.event.respository;

import com.vivekvarma1.moviebooking.event.entity.Language;
import com.vivekvarma1.moviebooking.event.entity.Movie;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    Optional<Movie> findByMovieNameIgnoreCase(String movieName);

    List<Movie> findByMovieNameContainingIgnoreCase(String query);

    boolean existsByMovieNameIgnoreCase(String movieName);

    boolean existsByMovieNameIgnoreCaseAndLanguage(
            String movieName,
            Language language
    );


}
