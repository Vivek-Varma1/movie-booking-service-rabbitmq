package com.vivekvarma1.moviebooking.pricing.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class MovieAgePricingRule implements PricingRule {

    @Override
    public BigDecimal apply(BigDecimal currentPrice,
                            PricingContext context) {

        LocalDate release =
                context.getMovie().getReleaseDate();

        if (release.isBefore(LocalDate.now().minusWeeks(3))) {

            return currentPrice.multiply(BigDecimal.valueOf(0.70));
        }

        return currentPrice;
    }
}