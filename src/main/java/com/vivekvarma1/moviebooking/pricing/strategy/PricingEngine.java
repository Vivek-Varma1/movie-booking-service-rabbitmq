package com.vivekvarma1.moviebooking.pricing.strategy;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PricingEngine {

    private final List<PricingRule> rules;

    public PricingEngine(List<PricingRule> rules) {
        this.rules = rules;
    }

    public BigDecimal calculate(PricingContext context) {

        BigDecimal price = BigDecimal.ZERO;

        for (PricingRule rule : rules) {

            price = rule.apply(price, context);
        }

        return price;
    }
}