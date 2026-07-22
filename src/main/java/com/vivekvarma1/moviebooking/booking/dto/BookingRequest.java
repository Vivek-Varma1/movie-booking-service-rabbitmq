package com.vivekvarma1.moviebooking.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BookingRequest {
    private String userName;
    private String userEmail;
    private long showId;
    private List<Integer> seatIds;
}