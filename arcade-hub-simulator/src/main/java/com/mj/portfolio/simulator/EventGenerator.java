package com.mj.portfolio.simulator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * Generates weighted random arcade machine events.
 *
 * Weights:
 *   COIN_IN      70% - regular play, value $0.25-$10.00
 *   COIN_OUT     15% - payouts,       value $0.50-$50.00
 *   MAINTENANCE  10% - routine signal, no value
 *   ERROR         5% - fault,          no value
 */
public class EventGenerator {

    private final Random random = new Random();

    /**
     * Immutable event value object.
     */
    public record SimEvent(String type, Double value) {}

    public SimEvent generate(double errorRate) {
        int roll = random.nextInt(100);

        if (roll < 70) {
            double value = 0.25 + random.nextDouble() * 9.75;
            return new SimEvent("COIN_IN", round(value));
        } else if (roll < 85) {
            double value = 0.50 + random.nextDouble() * 49.50;
            return new SimEvent("COIN_OUT", round(value));
        } else if (roll < 95) {
            return new SimEvent("MAINTENANCE", null);
        } else {
            return new SimEvent("ERROR", null);
        }
    }

    private double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
