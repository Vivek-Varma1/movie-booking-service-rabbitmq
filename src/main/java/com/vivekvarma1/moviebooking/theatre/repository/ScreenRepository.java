package com.vivekvarma1.moviebooking.theatre.repository;
import com.vivekvarma1.moviebooking.theatre.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
//    List<Screen> findByTheatreId(Long theatreId);

    List<Screen> findByTheatreIdOrderByNameAsc(
            Long theatreId
    );
    Optional<Screen> findByTheatreIdAndName(Long theatreId, String name);

    boolean existsByTheatreIdAndName(Long theatreId, String name);
    Optional<Screen> findByIdAndTheatreId(
            Long screenId,
            Long theatreId
    );
}