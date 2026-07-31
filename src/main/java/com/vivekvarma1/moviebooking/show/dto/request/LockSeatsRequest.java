package com.vivekvarma1.moviebooking.show.dto.request;

import java.util.List;

public record LockSeatsRequest(

//        Long userId,

        Long showId,

        List<Long> showSeatIds

) {}