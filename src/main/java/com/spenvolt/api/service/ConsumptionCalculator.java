package com.spenvolt.api.service;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ConsumptionCalculator {
    public double monthlyKwh(Double powerW, Double hoursPerDay, Integer daysPerMonth) {
        if (anyNull(powerW, hoursPerDay, daysPerMonth)) return 0;
        return powerW * hoursPerDay * daysPerMonth / 1000.0;
    }

    public double monthlyCost(double kwh, double tariff) {
        return kwh * tariff;
    }

    private boolean anyNull(Object... values) {
        return Arrays.asList(values).contains(null);
    }
}