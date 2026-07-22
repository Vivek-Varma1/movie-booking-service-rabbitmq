package com.vivekvarma1.moviebooking.show.entity;

import java.time.LocalTime;

public enum ShowSlot {

    MORNING(LocalTime.of(9, 0)),
    MATINEE(LocalTime.of(12, 0)),
    AFTERNOON(LocalTime.of(15, 0)),
    EVENING(LocalTime.of(18, 0)),
    NIGHT(LocalTime.of(21, 0));

    private final LocalTime startTime;

    ShowSlot(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }
}