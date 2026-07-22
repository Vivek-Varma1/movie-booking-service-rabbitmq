package com.vivekvarma1.moviebooking.theatre.entity;

import java.math.BigDecimal;

public enum SeatCategory {

    REGULAR(BigDecimal.valueOf(150)),
    PREMIUM(BigDecimal.valueOf(250)),
    RECLINER(BigDecimal.valueOf(400));

    private final BigDecimal basePrice;

    SeatCategory(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }
}