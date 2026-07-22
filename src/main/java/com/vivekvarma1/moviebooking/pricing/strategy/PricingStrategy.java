package com.vivekvarma1.moviebooking.pricing.strategy;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(PricingContext context);

}