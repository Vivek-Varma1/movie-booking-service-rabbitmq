package com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceNotFoundException;

public class TheatreNotFoundException
        extends ResourceNotFoundException {

    public TheatreNotFoundException(Long theatreId) {
        super(
                "Theatre",
                "id",
                theatreId
        );
    }
}