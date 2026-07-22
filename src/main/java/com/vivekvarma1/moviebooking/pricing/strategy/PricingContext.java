package com.vivekvarma1.moviebooking.pricing.strategy;

import com.vivekvarma1.moviebooking.event.entity.Movie;
import com.vivekvarma1.moviebooking.show.entity.Show;
import com.vivekvarma1.moviebooking.theatre.entity.Seat;

public class PricingContext {

    private final Movie movie;
    private final Show show;
    private final Seat seat;

    public PricingContext(Movie movie,
                          Show show,
                          Seat seat) {
        this.movie = movie;
        this.show = show;
        this.seat = seat;
    }

    public Movie getMovie() {
        return movie;
    }

    public Show getShow() {
        return show;
    }

    public Seat getSeat() {
        return seat;
    }
}