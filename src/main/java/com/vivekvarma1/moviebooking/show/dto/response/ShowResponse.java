package com.vivekvarma1.moviebooking.show.dto.response;

import com.vivekvarma1.moviebooking.show.entity.ShowSlot;
import com.vivekvarma1.moviebooking.show.entity.ShowStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowResponse {

    private Long showId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer durationInMinutes;

    private Long screenId;

    private String screenName;

    private Integer availableSeats;

    private Integer totalSeats;

    private BigDecimal minimumPrice;

    private ShowStatus status;

    private ShowSlot showSlot;
}