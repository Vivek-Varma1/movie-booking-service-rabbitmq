package com.vivekvarma1.moviebooking.event.response;

import com.vivekvarma1.moviebooking.event.entity.Certificate;
import com.vivekvarma1.moviebooking.event.entity.Language;

import java.time.LocalDate;

public record MovieSummaryResponse(

        Long id,

        String movieName,

        Language language,

        Integer durationInMinutes,

        Certificate certificate,

        LocalDate releaseDate,

        String posterUrl
) {}