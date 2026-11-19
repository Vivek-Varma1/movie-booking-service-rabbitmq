package com.vivekvarma1.moviebooking.booking.controller;

import com.vivekvarma1.moviebooking.booking.dto.response.AdminBookingResponse;
import com.vivekvarma1.moviebooking.booking.dto.response.AdminStatsResponse;
import com.vivekvarma1.moviebooking.booking.dto.response.PageResponse;
import com.vivekvarma1.moviebooking.booking.entity.BookingStatus;
import com.vivekvarma1.moviebooking.booking.service.AdminBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminBookingController {

    private final AdminBookingService adminBookingService;

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<AdminBookingResponse> bookings(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return adminBookingService.search(status, q, from, to, page, size);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminStatsResponse stats(@RequestParam(defaultValue = "30") int days) {
        return adminBookingService.stats(days);
    }
}