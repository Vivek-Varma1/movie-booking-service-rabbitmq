package com.vivekvarma1.moviebooking.show.repository;

import com.vivekvarma1.moviebooking.event.entity.Movie;
import com.vivekvarma1.moviebooking.show.entity.Show;

import com.vivekvarma1.moviebooking.show.entity.ShowSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {

    boolean existsByScreenIdAndShowDateAndShowSlot(
            Long screenId,
            LocalDate showDate,
            ShowSlot showSlot
    );

    List<Show> findAllByMovie(Movie movie);

    @Query("""
select distinct s.showDate
from Show s
join s.screen sc
join sc.theatre t
where s.movie.id = :movieId
and t.city.id = :cityId
and s.status =
com.vivekvarma1.moviebooking.show.entity.ShowStatus.OPEN_FOR_BOOKING
order by s.showDate
""")
    List<LocalDate> findAvailableDates(
            @Param("movieId") Long movieId,
            @Param("cityId") Long cityId
    );
    @Query("""
select distinct s
from Show s
join fetch s.screen sc
join fetch sc.theatre t
join fetch t.city c
where s.movie.id = :movieId
and c.id = :cityId
and s.showDate = :showDate
and s.status =
com.vivekvarma1.moviebooking.show.entity.ShowStatus.OPEN_FOR_BOOKING
order by t.name, s.showSlot
""")
    List<Show> findShowsByMovieAndCityAndDate(
            @Param("movieId") Long movieId,
            @Param("cityId") Long cityId,
            @Param("showDate") LocalDate showDate
    );
    @Query("""
select distinct s.movie
from Show s
join s.screen sc
join sc.theatre t
where t.city.id = :cityId
and s.status =
com.vivekvarma1.moviebooking.show.entity.ShowStatus.OPEN_FOR_BOOKING
order by s.movie.releaseDate desc
""")
    List<Movie> findMoviesByCity(
            @Param("cityId") Long cityId
    );
    @Query("""
select distinct s.movie
from Show s
join s.screen sc
join sc.theatre t
where lower(s.movie.movieName)
like lower(concat('%',:query,'%'))
and t.city.id=:cityId
and s.status =OPEN_FOR_BOOKING
""")
    List<Movie> searchMoviesByCity(
            String query,
            Long cityId
    );


    @Query("""
select distinct s
from Show s
left join fetch s.showSeats
join fetch s.movie
join fetch s.screen
where s.id = :showId
""")
    Optional<Show> findByIdWithDetails(Long showId);

    @Query("""
SELECT distinct s
FROM Show s
JOIN FETCH s.screen sc
JOIN FETCH sc.theatre t
WHERE s.movie = :movie
""")
    List<Show> findAllByMovieWithScreenAndTheatre(
            @Param("movie") Movie movie
    );
    boolean existsByScreenId(Long screenId);

    List<Show> findByMovieId(Long movieId);

}



//    @Query("""
//SELECT COUNT(s) > 0
//FROM Show s
//WHERE s.screen.id = :screenId
//AND s.status <> com.vivekvarma1.moviebooking.show.entity.ShowStatus.CANCELLED
//AND s.startTime < :newEndTime
//AND s.endTime > :newStartTime
//""")
//    boolean existsConflictingShow(
//            Long screenId,
//            LocalDateTime newStartTime,
//            LocalDateTime newEndTime
//    );
//
//    @Query("""
//    SELECT s
//    FROM Show s
//    JOIN FETCH s.screen sc
//    JOIN FETCH sc.theatre
//    WHERE s.movie = :movie
//""")
//    List<Show> findAllByMovieWithScreenAndTheatre(
//            @Param("movie") Movie movie
//    );
//
////    boolean existsByScreenIdAndStartTimeLessThanAndEndTimeGreaterThan(
////            Long screenId,
////            LocalDateTime endTime,
////            LocalDateTime startTime
////    );
//List<Show> findAllByMovie(Movie movie);
//
//}