package com.vivekvarma1.moviebooking.event.mapper;

import com.vivekvarma1.moviebooking.event.entity.Movie;
import com.vivekvarma1.moviebooking.event.request.CreateMovieRequest;
import com.vivekvarma1.moviebooking.event.request.UpdateMovieRequest;
import com.vivekvarma1.moviebooking.event.response.MovieResponse;
import com.vivekvarma1.moviebooking.event.response.MovieSummaryResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE
)
public interface MovieMapper {

    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Movie toEntity(CreateMovieRequest request);

    MovieResponse toResponse(Movie movie);

    MovieSummaryResponse toSummaryResponse(Movie movie);

    List<MovieSummaryResponse> toSummaryResponses(
            List<Movie> movies
    );

    List<MovieResponse> toResponses(
            List<Movie> movies
    );

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateMovieFromRequest(
            UpdateMovieRequest request,
            @MappingTarget Movie movie
    );
}