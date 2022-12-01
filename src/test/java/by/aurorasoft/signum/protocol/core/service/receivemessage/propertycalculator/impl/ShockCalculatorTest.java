package by.aurorasoft.signum.protocol.core.service.receivemessage.propertycalculator.impl;

import by.aurorasoft.signum.crud.model.dto.Message;
import org.junit.Test;

import java.util.Map;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;
import static org.junit.Assert.assertEquals;

public final class ShockCalculatorTest {
    private final ShockCalculator calculator;

    public ShockCalculatorTest() {
        this.calculator = new ShockCalculator();
    }

    @Test
    public void shockShouldBeCalculatedInCaseExistingAccelerometerParameters() {
        final Message givenCurrentMessage = Message.builder()
                .parameterNamesByValues(Map.of(ACC_X, 3., ACC_Y, 4., ACC_Z, 0.))
                .build();

        this.calculator.calculate(givenCurrentMessage);

        final double actual = givenCurrentMessage.getShock();
        final double expected = 5.;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void shockShouldBeCalculatedInCaseNotExistingAccelerometerParameters() {
        final Message givenCurrentMessage = Message.builder().build();

        this.calculator.calculate(givenCurrentMessage);

        final double actual = givenCurrentMessage.getShock();
        final double expected = 0.;
        assertEquals(expected, actual, 0.);
    }
}
