package com.vivekvarma1.moviebooking.show.dto.request;

import com.vivekvarma1.moviebooking.show.entity.ShowSlot;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateShowRequest(

        @NotNull
        Long movieId,

        @NotNull
        Long screenId,

        @NotNull
        @FutureOrPresent
        LocalDate showDate,

        @NotNull
        ShowSlot showSlot
) {
}