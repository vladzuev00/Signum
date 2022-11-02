package by.aurorasoft.signum.crud.service.message;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;
import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.*;
import static java.time.Instant.MIN;
import static java.time.Instant.now;
import static org.junit.Assert.assertSame;

public final class MessageValidatorTest extends AbstractContextTest {

    @Autowired
    private MessageValidator validator;

    @Test
    public void messageShouldBeValid() {
        final Message givenMessage = new Message(now(), new Message.GpsCoordinate(5.5F, 6.6F),
                7, 8, 10, 11, Map.of(HDOP, 6F, VDOP, 4F, PDOP, -1F));

        final MessageType actual = this.validator.validate(givenMessage);
        assertSame(VALID, actual);
    }

    @Test
    public void messageShouldBeCorrectBecauseOfNotValidAmountSatellite() {
        final Message givenMessage = new Message(now(), new Message.GpsCoordinate(5.5F, 6.6F),
                7, 8, 10, 2, Map.of(HDOP, 6F, VDOP, 4F, PDOP, -1F));

        final MessageType actual = this.validator.validate(givenMessage);
        assertSame(CORRECT, actual);
    }

    @Test
    public void messageShouldBeCorrectBecauseOfNotValidCoordinate() {
        final Message givenMessage = new Message(now(), new Message.GpsCoordinate(5.5F, 6.6F),
                7, 8, 10, 11, Map.of(HDOP, 8F, VDOP, 4F, PDOP, -1F));

        final MessageType actual = this.validator.validate(givenMessage);
        assertSame(CORRECT, actual);
    }

    @Test
    public void messageShouldBeIncorrectBecauseOfNotValidTime() {
        final Message givenMessage = new Message(MIN, new Message.GpsCoordinate(5.5F, 6.6F),
                7, 8, 10, 3, Map.of(HDOP, 6F, VDOP, 4F, PDOP, -1F));

        final MessageType actual = this.validator.validate(givenMessage);
        assertSame(INCORRECT, actual);
    }
}
