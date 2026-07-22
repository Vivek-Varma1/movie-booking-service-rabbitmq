package com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException;

public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(Long movieId) {
        super("Movie with ID " + movieId + " not found.");
    }
}