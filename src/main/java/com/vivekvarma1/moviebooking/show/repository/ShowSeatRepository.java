package com.vivekvarma1.moviebooking.show.repository;

import com.vivekvarma1.moviebooking.show.entity.ShowSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShowSeatRepository
        extends JpaRepository<ShowSeat, Long> {

//    List<ShowSeat> findByShowIdOrderBySeatRowAscSeatSeatNumberAsc(
//            Long showId
//    );
    @Query("""
            select ss
            from ShowSeat ss
            join fetch ss.seat seat
            where ss.show.id = :showId
            order by seat.row,
                     seat.seatNumber
            """)
    List<ShowSeat> findByShowIdOrderBySeatRowAscSeatSeatNumberAsc(
            Long showId
    );

    Optional<ShowSeat> findByShowIdAndSeatId(
            Long showId,
            Long seatId
    );

    List<ShowSeat> findByShowId(Long showId);

    List<ShowSeat> findAllByIdIn(
            List<Long> ids
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select ss
        from ShowSeat ss
        where ss.id = :id
    """)
    Optional<ShowSeat> lockById(Long id);

    @Query("""
    select ss
    from ShowSeat ss
    where ss.show.id = :showId
      and ss.id in :seatIds
    """)
    List<ShowSeat> findAllByShowIdAndIdIn(
            Long showId,
            List<Long> seatIds
    );

    List<ShowSeat> findAllByShowIdAndLockedById(Long showId, Long userId);

    @Query("""
        select ss.id
        from ShowSeat ss
        where ss.show.id = :showId
        """)
    List<Long> findIdsByShowId(@Param("showId") Long showId);

    @Query("""
       SELECT ss
       FROM ShowSeat ss
       WHERE ss.lockedBy IS NOT NULL
       AND ss.lockedUntil < :now
       """)
    List<ShowSeat> findExpiredLocks(
            LocalDateTime now
    );

    @Modifying
    @Query("""
UPDATE ShowSeat ss
SET ss.status = com.vivekvarma1.moviebooking.show.entity.ShowSeatStatus.AVAILABLE,
    ss.lockedBy = null,
    ss.lockedUntil = null
""")
    int unlockAllSeats();

    @Modifying
    @Query("""
UPDATE ShowSeat ss
SET ss.status = com.vivekvarma1.moviebooking.show.entity.ShowSeatStatus.AVAILABLE,
    ss.lockedBy = null,
    ss.lockedUntil = null
WHERE ss.id = :seatId
""")
    int unlockSeat(Long seatId);


}