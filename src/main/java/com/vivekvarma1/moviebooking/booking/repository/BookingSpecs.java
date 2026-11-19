package com.vivekvarma1.moviebooking.booking.repository;

import com.vivekvarma1.moviebooking.booking.entity.Booking;
import com.vivekvarma1.moviebooking.booking.entity.BookingStatus;
import com.vivekvarma1.moviebooking.event.entity.Movie;
import com.vivekvarma1.moviebooking.show.entity.Show;
import com.vivekvarma1.moviebooking.ticket.entity.Ticket;
import com.vivekvarma1.moviebooking.user.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//import java.util.function.Predicate;

public final class BookingSpecs {

    private BookingSpecs() {}

    public static Specification<Booking> filter(BookingStatus status, String q,
                                                LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            List<Predicate> all = new ArrayList<>();

            if (status != null) all.add(cb.equal(root.get("bookingStatus"), status));
            if (from != null)   all.add(cb.greaterThanOrEqualTo(root.get("bookedAt"), from.atStartOfDay()));
            if (to != null)     all.add(cb.lessThan(root.get("bookedAt"), to.plusDays(1).atStartOfDay()));

            if (q != null && !q.isBlank()) {
                String term = q.trim().toLowerCase();
                String like = "%" + term + "%";
                Join<Booking, User> user = root.join("user", JoinType.LEFT);
                Join<Booking, Show> show = root.join("show", JoinType.LEFT);
                Join<Show, Movie> movie = show.join("movie", JoinType.LEFT);
                Join<Booking, Ticket> ticket = root.join("ticket", JoinType.LEFT);

                List<Predicate> any = new ArrayList<>();
                any.add(cb.like(cb.lower(user.get("email")), like));
                any.add(cb.like(cb.lower(cb.coalesce(user.<String>get("name"), "")), like));
                any.add(cb.like(cb.lower(movie.get("movieName")), like));
                any.add(cb.like(cb.lower(cb.coalesce(ticket.<String>get("ticketNumber"), "")), like));
                String digits = term.replace("#", "");
                if (digits.matches("\\d{1,18}")) {
                    any.add(cb.equal(root.get("id"), Long.parseLong(digits)));
                }
                all.add(cb.or(any.toArray(new Predicate[0])));
                query.distinct(true);
            }
            return cb.and(all.toArray(new Predicate[0]));
        };
    }
}