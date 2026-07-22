package com.vivekvarma1.moviebooking.event.dto;

import com.vivekvarma1.moviebooking.event.entity.Certificate;
import com.vivekvarma1.moviebooking.event.entity.Genre;
import com.vivekvarma1.moviebooking.event.entity.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {

    private Long id;

    private String movieName;

    private int durationInMinutes;

    private Language language;

    private Set<Genre> genres;

    private Certificate certificate;

    private LocalDate releaseDate;

    private String posterUrl;

    private String synopsis;
}