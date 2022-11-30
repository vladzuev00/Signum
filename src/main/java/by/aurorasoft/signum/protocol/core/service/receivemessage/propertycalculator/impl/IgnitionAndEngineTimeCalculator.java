package by.aurorasoft.signum.protocol.core.service.receivemessage.propertycalculator.impl;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.core.service.receivemessage.propertycalculator.MessagePropertyCalculator;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.VOLTAGE;
import static java.lang.Double.compare;
import static java.time.Duration.between;

@Component
public final class IgnitionAndEngineTimeCalculator implements MessagePropertyCalculator {
    private static final double ONBOARD_VOLTAGE_INDICATING_IGNITION_ON = 13;
    private static final int IGNITION_ON_VALUE = 1;
    private static final int IGNITION_OFF_VALUE = 0;
    private static final long ENGINE_TIME_IN_CASE_NOT_EXIST_PREVIOUS_MESSAGE = 0;

    @Override
    public void calculate(Message current) {
        calculateIgnition(current);
        calculateEngineTime(current);
    }

    @Override
    public void calculate(Message current, Message previous) {
        calculateIgnition(current);
        calculateEngineTime(current, previous);
    }

    private static void calculateIgnition(Message current) {
        final double onboardVoltage = current.getParameter(VOLTAGE);
        current.setIgnition(compare(onboardVoltage, ONBOARD_VOLTAGE_INDICATING_IGNITION_ON) >= 0
                ? IGNITION_ON_VALUE
                : IGNITION_OFF_VALUE);
    }

    private static void calculateEngineTime(Message current) {
        current.setEngineTime(ENGINE_TIME_IN_CASE_NOT_EXIST_PREVIOUS_MESSAGE);
    }

    private static void calculateEngineTime(Message current, Message previous) {
        current.setEngineTime(
                previous.getEngineTime()
                        + (isIgnitionOnBetweenMessages(previous, current)
                        ? between(previous.getDatetime(), current.getDatetime()).toSeconds()
                        : 0));
    }

    private static boolean isIgnitionOnBetweenMessages(Message current, Message previous) {
        return previous.getIgnition() == IGNITION_ON_VALUE && current.getIgnition() == IGNITION_ON_VALUE;
    }
}
