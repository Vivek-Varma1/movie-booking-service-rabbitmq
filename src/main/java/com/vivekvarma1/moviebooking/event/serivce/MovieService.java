package com.vivekvarma1.moviebooking.event.serivce;

import com.vivekvarma1.moviebooking.event.request.CreateMovieRequest;
import com.vivekvarma1.moviebooking.event.request.UpdateMovieRequest;
import com.vivekvarma1.moviebooking.event.response.MovieDateShowsResponse;
import com.vivekvarma1.moviebooking.event.response.MovieResponse;
import com.vivekvarma1.moviebooking.event.response.MovieSummaryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
public interface MovieService {

    MovieResponse createMovie(
            CreateMovieRequest request,
            MultipartFile posterFile
    );

    MovieResponse updateMovie(
            Long movieId,
            UpdateMovieRequest request,
            MultipartFile posterFile
    );

    void deleteMovie(
            Long movieId
    );

    MovieResponse getMovieById(
            Long movieId
    );

    List<MovieSummaryResponse> getAllMovies(
            Long cityId
    );

    List<MovieSummaryResponse> searchMovies(
            String query,
            Long cityId
    );

    List<LocalDate> getMovieShowDates(
            Long movieId,
            Long cityId
    );

    MovieDateShowsResponse getMovieShows(
            Long movieId,
            Long cityId,
            LocalDate showDate
    );
}