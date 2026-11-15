package com.vivekvarma1.moviebooking.show.dto.response;

import java.time.Instant;
import java.util.List;

public record LockSeatsResponse(
        List<Long> lockedSeatIds,
        Instant lockedUntil
) {}