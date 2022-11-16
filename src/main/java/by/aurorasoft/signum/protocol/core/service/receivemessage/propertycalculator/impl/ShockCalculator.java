package by.aurorasoft.signum.protocol.core.service.receivemessage.propertycalculator.impl;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.core.service.receivemessage.propertycalculator.MessagePropertyCalculator;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

@Component
public final class ShockCalculator implements MessagePropertyCalculator {

    @Override
    public void calculate(Message current) {
        current.setShock(calculateShock(current));
    }

    @Override
    public void calculate(Message current, Message previous) {
        this.calculate(current);
    }

    private static double calculateShock(Message message) {
        final double accX = message.getParameterOrDefault(ACC_X, 0.);
        final double accY = message.getParameterOrDefault(ACC_Y, 0.);
        final double accZ = message.getParameterOrDefault(ACC_Z, 0.);
        return sqrt(pow(accX, 2) + pow(accY, 2) + pow(accZ, 2));
    }
}
