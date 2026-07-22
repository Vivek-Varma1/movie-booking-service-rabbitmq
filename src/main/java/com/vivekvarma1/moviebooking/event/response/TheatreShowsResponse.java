package com.vivekvarma1.moviebooking.event.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder

public class TheatreShowsResponse {

    private Long theatreId;

    private String theatreName;

    private String address;

    private List<ShowTimeResponse> shows;
}