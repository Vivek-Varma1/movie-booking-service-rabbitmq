package com.vivekvarma1.moviebooking.pricing.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SeatTypePricingRule implements PricingRule {

    @Override
    public BigDecimal apply(BigDecimal price,
                            PricingContext context) {

        return switch (context.getSeat().getSeatCategory()) {

            case REGULAR ->
                    BigDecimal.valueOf(150);

            case PREMIUM ->
                    BigDecimal.valueOf(250);

            case RECLINER ->
                    BigDecimal.valueOf(500);
        };
    }
}