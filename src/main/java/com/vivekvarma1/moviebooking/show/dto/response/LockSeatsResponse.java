package com.vivekvarma1.moviebooking.show.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record LockSeatsResponse(

        List<Long> lockedSeatIds,

        LocalDateTime lockedUntil

) {}