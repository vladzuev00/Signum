package by.aurorasoft.signum.crud.service.message;

import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import org.springframework.stereotype.Component;

import static java.lang.Float.compare;
import static java.time.Duration.between;

@Component
public final class IgnitionAndEngineTimeCalculator implements MessagePropertyCalculator {
    private static final float ONBOARD_VOLTAGE_INDICATING_IGNITION_ON = 13;
    private static final int IGNITION_ON_VALUE = 1;
    private static final int IGNITION_OFF_VALUE = 0;
    private static final long ENGINE_TIME_IN_CASE_NOT_EXISTING_LAST_MESSAGE = 0;

    @Override
    public void calculate(MessageEntity current) {
        calculateIgnition(current);
        calculateEngineTime(current);
    }

    @Override
    public void calculate(MessageEntity last, MessageEntity current) {
        calculateIgnition(current);
        calculateEngineTime(last, current);
    }

    private static void calculateIgnition(MessageEntity current) {
        current.setIgnition(compare(current.getOnboardVoltage(), ONBOARD_VOLTAGE_INDICATING_IGNITION_ON) >= 0
                ? IGNITION_ON_VALUE
                : IGNITION_OFF_VALUE);
    }

    private static void calculateEngineTime(MessageEntity current) {
        current.setEngineTime(ENGINE_TIME_IN_CASE_NOT_EXISTING_LAST_MESSAGE);
    }

    private static void calculateEngineTime(MessageEntity last, MessageEntity current) {
        current.setEngineTime(
                last.getEngineTime()
                        + (isIgnitionOnBetweenMessages(last, current)
                        ? between(current.getDatetime(), last.getDatetime()).toSeconds()
                        : 0));
    }

    private static boolean isIgnitionOnBetweenMessages(MessageEntity last, MessageEntity current) {
        return last.getIgnition() == IGNITION_ON_VALUE && current.getIgnition() == IGNITION_ON_VALUE;
    }
}
