package by.aurorasoft.signum.protocol.core.service.receivemessage.propertycalculator.impl;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.ParameterName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.VOLTAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class IgnitionAndEngineTimeCalculatorTest {

    private final IgnitionAndEngineTimeCalculator calculator;

    @Captor
    private ArgumentCaptor<Integer> integerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<ParameterName> parameterNameArgumentCaptor;

    public IgnitionAndEngineTimeCalculatorTest() {
        this.calculator = new IgnitionAndEngineTimeCalculator();
    }

    @Test
    public void ignitionWhichIsOnAndEngineTimeShouldBeCalculatedInCaseNotExistingPreviousMessage() {
        final Message givenCurrentMessage = mock(Message.class);

        final double givenVoltageParameterValue = 13.;
        when(givenCurrentMessage.getParameter(any(ParameterName.class))).thenReturn(givenVoltageParameterValue);

        this.calculator.calculate(givenCurrentMessage);

        verify(givenCurrentMessage, times(1))
                .getParameter(this.parameterNameArgumentCaptor.capture());
        verify(givenCurrentMessage, times(1)).setIgnition(this.integerArgumentCaptor.capture());
        verify(givenCurrentMessage, times(1)).setEngineTime(this.longArgumentCaptor.capture());

        assertSame(VOLTAGE, this.parameterNameArgumentCaptor.getValue());
        assertEquals(1, this.integerArgumentCaptor.getValue().intValue());
        assertEquals(0, this.longArgumentCaptor.getValue().longValue());
    }

    @Test
    public void ignitionWhichIsOffAndEngineTimeShouldBeCalculatedInCaseNotExistingPreviousMessage() {
        final Message givenCurrentMessage = mock(Message.class);

        final double givenVoltageParameterValue = 12.99999999;
        when(givenCurrentMessage.getParameter(any(ParameterName.class))).thenReturn(givenVoltageParameterValue);

        this.calculator.calculate(givenCurrentMessage);

        verify(givenCurrentMessage, times(1))
                .getParameter(this.parameterNameArgumentCaptor.capture());
        verify(givenCurrentMessage, times(1)).setIgnition(this.integerArgumentCaptor.capture());
        verify(givenCurrentMessage, times(1)).setEngineTime(this.longArgumentCaptor.capture());

        assertSame(VOLTAGE, this.parameterNameArgumentCaptor.getValue());
        assertEquals(0, this.integerArgumentCaptor.getValue().intValue());
        assertEquals(0, this.longArgumentCaptor.getValue().longValue());
    }
}
