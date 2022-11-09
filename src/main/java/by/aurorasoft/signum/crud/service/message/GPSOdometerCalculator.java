package by.aurorasoft.signum.crud.service.message;

import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.nhorushko.distancecalculator.DistanceCalculator;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
final class GPSOdometerCalculator implements MessagePropertyCalculator {
    private final DistanceCalculatorSettings distanceCalculatorSettings;
    private final DistanceCalculator distanceCalculator;

    @Override
    public void calculate(MessageEntity last, MessageEntity current) {
        final double gpsOdometer = this.distanceCalculator
                .calculateDistance(last, current, this.distanceCalculatorSettings);
        current.setGpsOdometer(gpsOdometer);
    }
}
