package com.vivekvarma1.moviebooking.event.response;

import com.vivekvarma1.moviebooking.show.entity.ShowSlot;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ShowTimeResponse {

    private Long showId;

    private ShowSlot showSlot;

    private LocalDateTime startTime;

    private Integer availableSeats;

    private BigDecimal minimumPrice;

    private String screenName;
}