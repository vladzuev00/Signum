package by.aurorasoft.signum.config;

import by.nhorushko.distancecalculator.DistanceCalculator;
import by.nhorushko.distancecalculator.DistanceCalculatorImpl;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import by.nhorushko.distancecalculator.DistanceCalculatorSettingsImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DistanceCalculationConfig {
    private static final int MIN_DETECTION_SPEED = 0;
    private static final int MAX_MESSAGE_TIMEOUT = 3600;

    @Bean
    public DistanceCalculatorSettings createDistanceCalculatorSettings() {
        return new DistanceCalculatorSettingsImpl(MIN_DETECTION_SPEED, MAX_MESSAGE_TIMEOUT);
    }

    @Bean
    public DistanceCalculator createDistanceCalculator() {
        return new DistanceCalculatorImpl();
    }
}
