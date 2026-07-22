package com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceNotFoundException;

public class ShowNotFoundException
        extends ResourceNotFoundException {

    public ShowNotFoundException(Long showId) {
        super(
                "Show",
                "id",
                showId
        );
    }
}