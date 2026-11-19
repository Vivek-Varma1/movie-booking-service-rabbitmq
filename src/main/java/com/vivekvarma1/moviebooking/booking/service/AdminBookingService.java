package com.vivekvarma1.moviebooking.booking.service;

import com.vivekvarma1.moviebooking.booking.dto.response.AdminBookingResponse;
import com.vivekvarma1.moviebooking.booking.dto.response.AdminStatsResponse;
import com.vivekvarma1.moviebooking.booking.dto.response.PageResponse;
import com.vivekvarma1.moviebooking.booking.entity.Booking;
import com.vivekvarma1.moviebooking.booking.entity.BookingStatus;
import com.vivekvarma1.moviebooking.booking.repository.BookingRepository;
import com.vivekvarma1.moviebooking.booking.repository.BookingSpecs;
import com.vivekvarma1.moviebooking.show.entity.Show;
import com.vivekvarma1.moviebooking.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminBookingService {

    private final BookingRepository bookingRepository;

    public PageResponse<AdminBookingResponse> search(BookingStatus status, String q,
                                                     LocalDate from, LocalDate to,
                                                     int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), 50);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize,
                Sort.by(Sort.Direction.DESC, "bookedAt"));

        Page<Booking> result = bookingRepository.findAll(BookingSpecs.filter(status, q, from, to), pageable);

        return new PageResponse<>(
                result.getContent().stream().map(this::toDto).toList(),
                result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
    }

    public AdminStatsResponse stats(int days) {
        int d = Math.min(Math.max(days, 1), 365);
        LocalDate today = LocalDate.now();
        LocalDateTime from = today.minusDays(d - 1L).atStartOfDay();
        BookingStatus ok = BookingStatus.CONFIRMED;

        Map<LocalDate, BigDecimal> revenueByDay = new HashMap<>();
        Map<LocalDate, Long> countByDay = new HashMap<>();
        for (Object[] row : bookingRepository.amountsSince(ok, from)) {
            LocalDate day = ((LocalDateTime) row[0]).toLocalDate();
            revenueByDay.merge(day, (BigDecimal) row[1], BigDecimal::add);
            countByDay.merge(day, 1L, Long::sum);
        }
        List<AdminStatsResponse.DailyPoint> daily = new ArrayList<>();
        for (int i = 0; i < d; i++) {                       // fill empty days with zero
            LocalDate day = from.toLocalDate().plusDays(i);
            daily.add(new AdminStatsResponse.DailyPoint(
                    day, revenueByDay.getOrDefault(day, BigDecimal.ZERO), countByDay.getOrDefault(day, 0L)));
        }

        List<AdminStatsResponse.TopMovie> top = bookingRepository
                .topMovies(ok, from, PageRequest.of(0, 5)).stream()
                .map(r -> new AdminStatsResponse.TopMovie(
                        (String) r[0], String.valueOf(r[1]), (BigDecimal) r[2], (Long) r[3]))
                .toList();

        return new AdminStatsResponse(
                d,
                bookingRepository.sumRevenueSince(ok, from),
                bookingRepository.sumRevenueSince(ok, today.atStartOfDay()),
                bookingRepository.countSince(ok, from),
                bookingRepository.countSeatsSince(ok, from),
                bookingRepository.countByBookingStatus(BookingStatus.PAYMENT_STARTED),
                daily, top);
    }

    private AdminBookingResponse toDto(Booking b) {
        Show show = b.getShow();
        User u = b.getUser();
        return new AdminBookingResponse(
                b.getId(), b.getBookingStatus(), b.getTotalAmount(), b.getBookedAt(),
                u.getId(), u.getName(), u.getEmail(), u.getPhoneNumber(),
                show.getMovie().getMovieName(),
                show.getScreen().getTheatre().getName(),
                show.getScreen().getName(),
                show.getStartDateTime(),
                b.getBookingSeats().stream()
                        .map(bs -> bs.getShowSeat().getSeat().getSeatLabel()).toList(),
                b.getTicket() != null ? b.getTicket().getTicketNumber() : null);
    }
}