package by.aurorasoft.signum.protocol.core.service.receivemessage.propertycalculator.impl;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.core.service.receivemessage.propertycalculator.MessagePropertyCalculator;
import by.nhorushko.distancecalculator.DistanceCalculator;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class GPSOdometerCalculator implements MessagePropertyCalculator {
    private static final double GPS_ODOMETER_IN_CASE_NOT_EXIST_PREVIOUS_MESSAGE = 0;

    private final DistanceCalculatorSettings distanceCalculatorSettings;
    private final DistanceCalculator distanceCalculator;

    @Override
    public void calculate(Message current) {
        current.setGpsOdometer(GPS_ODOMETER_IN_CASE_NOT_EXIST_PREVIOUS_MESSAGE);
    }

    @Override
    public void calculate(Message current, Message previous) {
        final double gpsOdometer = this.distanceCalculator
                .calculateDistance(previous, current, this.distanceCalculatorSettings);
        current.setGpsOdometer(gpsOdometer);
    }
}
