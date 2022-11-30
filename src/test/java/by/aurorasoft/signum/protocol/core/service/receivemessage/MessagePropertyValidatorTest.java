package by.aurorasoft.signum.protocol.core.service.receivemessage;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Message;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;
import static java.time.Instant.now;
import static java.time.Instant.parse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class MessagePropertyValidatorTest extends AbstractContextTest {

    @Autowired
    private MessagePropertyValidator validator;

    @Test
    public void amountSatelliteShouldBeValid() {
        final Message givenMessage = Message.builder()
                .amountSatellite(3)
                .build();
        assertTrue(this.validator.isValidAmountSatellite(givenMessage));
    }

    @Test
    public void amountSatelliteShouldNotBeValid() {
        final Message givenMessage = Message.builder()
                .amountSatellite(2)
                .build();
        assertFalse(this.validator.isValidAmountSatellite(givenMessage));
    }

    @Test
    public void coordinateParametersShouldBeValid() {
        final Message givenMessage = Message.builder()
                .parameterNamesByValues(Map.of(HDOP, 7., VDOP, 0., PDOP, 1.))
                .build();
        assertTrue(this.validator.areValidCoordinateParameters(givenMessage));
    }

    @Test
    public void coordinateParametersShouldNotBeValidBecauseOfThereIsNoHDOPParameter() {
        final Message givenMessage = Message.builder()
                .parameterNamesByValues(Map.of(VDOP, 0., PDOP, 1.))
                .build();
        assertFalse(this.validator.areValidCoordinateParameters(givenMessage));
    }

    @Test
    public void coordinateParametersShouldNotBeValidBecauseOfThereIsNoVDOPParameter() {
        final Message givenMessage = Message.builder()
                .parameterNamesByValues(Map.of(HDOP, 7., PDOP, 1.))
                .build();
        assertFalse(this.validator.areValidCoordinateParameters(givenMessage));
    }

    @Test
    public void coordinateParametersShouldNotBeValidBecauseOfThereIsNoPDOPParameter() {
        final Message givenMessage = Message.builder()
                .parameterNamesByValues(Map.of(HDOP, 7., VDOP, 0.))
                .build();
        assertFalse(this.validator.areValidCoordinateParameters(givenMessage));
    }

    @Test
    public void coordinateParametersShouldNotBeValidBecauseOfNotValidHDOPParameter() {
        final Message givenMessage = Message.builder()
                .parameterNamesByValues(Map.of(HDOP, 7.001, VDOP, 0., PDOP, 1.))
                .build();
        assertFalse(this.validator.areValidCoordinateParameters(givenMessage));
    }

    @Test
    public void coordinateParametersShouldNotBeValidBecauseOfNotValidVDOPParameter() {
        final Message givenMessage = Message.builder()
                .parameterNamesByValues(Map.of(HDOP, 7., VDOP, -0.0001, PDOP, 1.))
                .build();
        assertFalse(this.validator.areValidCoordinateParameters(givenMessage));
    }

    @Test
    public void coordinateParametersShouldNotBeValidBecauseOfNotValidPDOPParameter() {
        final Message givenMessage = Message.builder()
                .parameterNamesByValues(Map.of(HDOP, 7., VDOP, 0., PDOP, 7.1))
                .build();
        assertFalse(this.validator.areValidCoordinateParameters(givenMessage));
    }

    @Test
    public void dateTimeShouldBeValid() {
        final Message givenMessage = Message.builder()
                .datetime(now())
                .build();
        assertTrue(this.validator.isValidDateTime(givenMessage));
    }

    @Test
    public void dateTimeShouldNotBeValid() {
        final Message givenMessage = Message.builder()
                .datetime(parse("2019-01-01T00:00:00Z"))
                .build();
        assertFalse(this.validator.isValidDateTime(givenMessage));
    }
}
