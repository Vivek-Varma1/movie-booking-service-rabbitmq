package com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceNotFoundException;

public class ScreenNotFoundException
        extends ResourceNotFoundException {

    public ScreenNotFoundException(Long screenId) {
        super(
                "Screen",
                "id",
                screenId
        );
    }
}