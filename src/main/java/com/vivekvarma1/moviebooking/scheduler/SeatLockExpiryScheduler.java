package com.vivekvarma1.moviebooking.scheduler;

import com.vivekvarma1.moviebooking.booking.entity.Booking;
import com.vivekvarma1.moviebooking.booking.entity.BookingStatus;
import com.vivekvarma1.moviebooking.booking.repository.BookingRepository;
import com.vivekvarma1.moviebooking.show.entity.ShowSeat;
import com.vivekvarma1.moviebooking.show.repository.ShowSeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatLockExpiryScheduler {

    private final ShowSeatRepository showSeatRepository;
    private final BookingRepository bookingRepository;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void releaseExpiredLocks() {

        LocalDateTime now = LocalDateTime.now();

        List<ShowSeat> expiredSeats =
                showSeatRepository.findExpiredLocks(now);

        if (expiredSeats.isEmpty()) {
            return;
        }

        log.info("Found {} expired seat locks", expiredSeats.size());

        for (ShowSeat showSeat : expiredSeats) {

            showSeat.unlock();

        }

        List<Booking> bookings =
                bookingRepository.findByBookingStatus(
                        BookingStatus.PAYMENT_STARTED
                );

        for (Booking booking : bookings) {

            boolean expired =
                    booking.getBookingSeats()
                            .stream()
                            .allMatch(bs ->
                                    bs.getShowSeat().isAvailable());

            if (expired) {

                booking.expire();

                log.info(
                        "Booking {} expired",
                        booking.getId()
                );
            }

        }

    }

}