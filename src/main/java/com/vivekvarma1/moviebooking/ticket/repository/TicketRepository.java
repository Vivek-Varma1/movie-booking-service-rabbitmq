package com.vivekvarma1.moviebooking.ticket.repository;

import com.vivekvarma1.moviebooking.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TicketRepository
        extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByBookingId(Long bookingId);

    Optional<Ticket> findByTicketNumber(String ticketNumber);

    @Modifying(clearAutomatically = true)
    @Query("update Ticket t set t.usedAt = :now where t.ticketNumber = :ticketNumber and t.usedAt is null")
    int markUsed(@Param("ticketNumber") String ticketNumber, @Param("now") LocalDateTime now);
}