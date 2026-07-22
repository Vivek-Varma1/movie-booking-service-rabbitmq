package com.vivekvarma1.moviebooking.pricing.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

@Component
public class WeekendPricingRule implements PricingRule {

    @Override
    public BigDecimal apply(BigDecimal currentPrice,
                            PricingContext context) {

        DayOfWeek day =
                context.getShow()
                       .getStartDateTime()
                       .getDayOfWeek();

        if (day == SATURDAY ||
            day == SUNDAY) {

            return currentPrice.add(BigDecimal.valueOf(40));
        }

        return currentPrice;
    }
}