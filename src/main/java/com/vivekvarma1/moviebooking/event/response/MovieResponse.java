package com.vivekvarma1.moviebooking.event.response;

import com.vivekvarma1.moviebooking.event.entity.Certificate;
import com.vivekvarma1.moviebooking.event.entity.Genre;
import com.vivekvarma1.moviebooking.event.entity.Language;
import com.vivekvarma1.moviebooking.event.entity.MovieStatus;

import java.time.LocalDate;
import java.util.Set;

public record MovieResponse(

        Long id,

        String movieName,

        Integer durationInMinutes,

        Language language,

        Set<Genre> genres,

        Certificate certificate,

        MovieStatus movieStatus,

        LocalDate releaseDate,

        String posterUrl,

        String trailerUrl,

        String synopsis
) {}