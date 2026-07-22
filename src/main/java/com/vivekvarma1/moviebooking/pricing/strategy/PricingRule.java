package com.vivekvarma1.moviebooking.pricing.strategy;

import java.math.BigDecimal;

public interface PricingRule {

    BigDecimal apply(BigDecimal currentPrice,
                     PricingContext context);

}