package com.vivekvarma1.moviebooking.theatre.repository;

import com.vivekvarma1.moviebooking.theatre.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreenId(Long screenId);

    Optional<Seat> findByScreenIdAndRowAndSeatNumber(
            Long screenId,
            String row,
            Integer seatNumber
    );

    boolean existsByScreenIdAndRowAndSeatNumber(
            Long screenId,
            String row,
            Integer seatNumber
    );
}