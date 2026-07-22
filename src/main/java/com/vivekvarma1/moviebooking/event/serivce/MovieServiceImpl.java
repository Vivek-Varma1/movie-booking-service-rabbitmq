package com.vivekvarma1.moviebooking.event.serivce;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceAlreadyExistsException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceNotFoundException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException.MovieNotFoundException;
import com.vivekvarma1.moviebooking.event.mapper.MovieMapper;
import com.vivekvarma1.moviebooking.event.request.CreateMovieRequest;
import com.vivekvarma1.moviebooking.event.request.UpdateMovieRequest;
import com.vivekvarma1.moviebooking.event.response.*;
import com.vivekvarma1.moviebooking.event.entity.Movie;
import com.vivekvarma1.moviebooking.event.respository.MovieRepository;
import com.vivekvarma1.moviebooking.show.dto.response.ShowResponse;
import com.vivekvarma1.moviebooking.show.entity.Show;
import com.vivekvarma1.moviebooking.show.mapper.ShowMapper;
import com.vivekvarma1.moviebooking.show.repository.ShowRepository;
import com.vivekvarma1.moviebooking.theatre.entity.City;
import com.vivekvarma1.moviebooking.theatre.entity.Theatre;
import com.vivekvarma1.moviebooking.theatre.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{
    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final MovieMapper movieMapper;
    private final ShowMapper showMapper;
    private final CityRepository cityRepository;





    @Transactional
    @Override
    public MovieResponse createMovie(CreateMovieRequest request) {

        if (movieRepository
                .existsByMovieNameIgnoreCaseAndLanguage(
                        request.movieName(),
                        request.language()
                )) {

            throw new ResourceAlreadyExistsException(
                    "Movie already exists with name '" +
                            request.movieName() +
                            "' and language '" +
                            request.language() +
                            "'"
            );
        }

        Movie movie = movieMapper.toEntity(request);

        Movie savedMovie =
                movieRepository.save(movie);

        return movieMapper.toResponse(savedMovie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalDate> getMovieShowDates(
            Long movieId,
            Long cityId
    ) {

        if (!movieRepository.existsById(movieId)) {
            throw new MovieNotFoundException(movieId);
        }

        return showRepository.findAvailableDates(
                movieId,
                cityId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public MovieDateShowsResponse getMovieShows(
            Long movieId,
            Long cityId,
            LocalDate showDate
    ) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() ->
                        new MovieNotFoundException(movieId));

        City city = cityRepository.findById(cityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "City",
                                "cityId",
                                cityId
                        ));

        List<Show> shows =
                showRepository.findShowsByMovieAndCityAndDate(
                        movieId,
                        cityId,
                        showDate
                );

        Map<Theatre, List<ShowResponse>> theatreMap =
                shows.stream()
                        .collect(
                                Collectors.groupingBy(
                                        show ->
                                                show.getScreen()
                                                        .getTheatre(),

                                        Collectors.mapping(
                                                showMapper::toResponse,
                                                Collectors.toList()
                                        )
                                )
                        );

        List<TheatreShowResponse> theatres =
                theatreMap.entrySet()
                        .stream()
                        .map(entry ->
                                new TheatreShowResponse(
                                        entry.getKey().getId(),
                                        entry.getKey().getName(),
                                        entry.getKey().getAddress(),
                                        entry.getValue()
                                )
                        )
                        .toList();

        return new MovieDateShowsResponse(
                movieMapper.toSummaryResponse(movie),

                new CitySummaryResponse(
                        city.getId(),
                        city.getName()
                ),

                showDate,

                theatres
        );
    }
    /*
    @Transactional
@Override
public MovieResponse createMovie(CreateMovieRequest request) {

    try {

        Movie movie = movieMapper.toEntity(request);

        Movie savedMovie = movieRepository.save(movie);

        return movieMapper.toResponse(savedMovie);

    } catch (DataIntegrityViolationException ex) {

        if (ex.getMostSpecificCause()
                .getMessage()
                .contains("uk_movie_name_language")) {

            throw new ResourceAlreadyExistsException(
                    "Movie already exists with name '" +
                            request.movieName() +
                            "' and language '" +
                            request.language() +
                            "'"
            );
        }

        throw ex;
    }
}
     */

    @Transactional
    @Override
    public MovieResponse updateMovie(
            Long id,
            UpdateMovieRequest request
    ) {

        Movie movie =
                movieRepository.findById(id)
                        .orElseThrow(() ->
                                new MovieNotFoundException(id));

        movieMapper.updateMovieFromRequest(
                request,
                movie
        );

        Movie updatedMovie =
                movieRepository.save(movie);

        return movieMapper.toResponse(
                updatedMovie
        );
    }

    @Transactional(readOnly = true)
    @Override
    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id).
                orElseThrow(() -> new MovieNotFoundException(id));
        return movieMapper.toResponse(movie);

    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieSummaryResponse> searchMovies(
            String query,
            Long cityId
    ) {

        return movieMapper.toSummaryResponses(
                showRepository.searchMoviesByCity(
                        query,
                        cityId
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieSummaryResponse> getAllMovies(
            Long cityId
    ) {

        return movieMapper.toSummaryResponses(
                showRepository.findMoviesByCity(cityId)
        );
    }

    @Transactional
    @Override
    public void deleteMovie(Long id) {

        if (!movieRepository.existsById(id)) {
            throw new MovieNotFoundException(id);
        }

        movieRepository.deleteById(id);
    }

//    @Transactional(readOnly = true)
//    @Override
//    public MovieShowResponse movieShows(Long movieId) {
//
//        Movie movie = movieRepository.findById(movieId)
//                .orElseThrow(() ->
//                        new MovieNotFoundException(movieId)
//                );
//
//        MovieSummaryResponse movieResponse =
//                movieMapper.toSummaryResponse(movie);
//
//        List<Show> shows =
//                showRepository.findAllByMovieWithScreenAndTheatre(movie);
//
//        Map<Theatre, List<ShowResponse>> theatreMap =
//                shows.stream()
//                        .collect(Collectors.groupingBy(
//                                show -> show.getScreen().getTheatre(),
//                                Collectors.mapping(
//                                        showMapper::toResponse,
//                                        Collectors.toList()
//                                )
//                        ));
//
//        List<TheatreShowResponse> theatres =
//                theatreMap.entrySet()
//                        .stream()
//                        .map(entry -> TheatreShowResponse.builder()
//                                .theatreId(entry.getKey().getId())
//                                .theatreName(entry.getKey().getName())
//                                .shows(entry.getValue())
//                                .build())
//                        .toList();
//
//        return MovieShowResponse.builder()
//                .movie(movieResponse)
//                .theatres(theatres)
//                .build();
//    }

    private Movie getMovieOrThrow(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() ->
                        new MovieNotFoundException(movieId)
                );
    }

    private City getCityOrThrow(Long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "City",
                                "cityId",
                                cityId
                        )
                );
    }
}
