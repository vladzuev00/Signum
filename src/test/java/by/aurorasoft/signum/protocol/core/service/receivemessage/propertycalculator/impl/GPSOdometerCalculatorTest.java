package by.aurorasoft.signum.protocol.core.service.receivemessage.propertycalculator.impl;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.nhorushko.distancecalculator.DistanceCalculator;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class GPSOdometerCalculatorTest {

    @Mock
    private DistanceCalculatorSettings mockedDistanceCalculatorSettings;

    @Mock
    private DistanceCalculator mockedDistanceCalculator;

    private GPSOdometerCalculator gpsOdometerCalculator;

    @Captor
    private ArgumentCaptor<Double> doubleArgumentCaptor;

    @Before
    public void initializeGpsOdometerCalculator() {
        this.gpsOdometerCalculator = new GPSOdometerCalculator(this.mockedDistanceCalculatorSettings,
                this.mockedDistanceCalculator);
    }

    @Test
    public void gpsOdometerShouldBeCalculatedInCaseNotExistPreviousMessage() {
        final Message givenCurrentMessage = mock(Message.class);

        this.gpsOdometerCalculator.calculate(givenCurrentMessage);

        verify(givenCurrentMessage, times(1)).setGpsOdometer(this.doubleArgumentCaptor.capture());
        assertEquals(0., this.doubleArgumentCaptor.getValue(), 0.);
    }

    @Test
    public void gpsOdometerShouldBeCalculatedInCaseExistPreviousMessage() {
        final Message givenPreviousMessage = mock(Message.class);
        final Message givenCurrentMessage = mock(Message.class);

        final double givenCalculatedDistance = 0.002;
        when(this.mockedDistanceCalculator.calculateDistance(any(Message.class), any(Message.class), any()))
                .thenReturn(givenCalculatedDistance);

        this.gpsOdometerCalculator.calculate(givenCurrentMessage, givenPreviousMessage);

        verify(givenCurrentMessage, times(1)).setGpsOdometer(this.doubleArgumentCaptor.capture());
        assertEquals(givenCalculatedDistance, this.doubleArgumentCaptor.getValue(), 0.);
    }
}
