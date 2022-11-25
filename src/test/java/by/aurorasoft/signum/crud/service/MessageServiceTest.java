package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;
import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.VALID;
import static java.time.Instant.parse;
import static org.junit.Assert.*;

public final class MessageServiceTest extends AbstractContextTest {
    private static final double ALLOWABLE_INACCURACY = 0.001;

    @Autowired
    private MessageService service;

    @Test
    @Sql("classpath:sql/insert-message.sql")
    public void lastMessageShouldBeFound() {
        final Long givenDeviceId = 25551L;

        final Optional<Message> optionalActual = this.service.findLastMessage(givenDeviceId);
        assertTrue(optionalActual.isPresent());
        final Message actual = optionalActual.get();

        final Message expected = Message.builder()
                .id(25551L)
                .datetime(parse("2000-02-18T04:05:06Z"))
                .coordinate(new GpsCoordinate(4.4F, 5.5F))
                .speed(10)
                .course(11)
                .altitude(12)
                .amountSatellite(13)
                .parameterNamesToValues(
                        Map.of(GSM_LEVEL, 44., VOLTAGE, 1.5, CORNER_ACCELERATION, 1.6,
                                ACCELERATION_UP, 1.7, ACCELERATION_DOWN, 1.8))
                .type(VALID)
                .gpsOdometer(100)
                .ignition(1)
                .engineTime(1000)
                .shock(2)
                .build();
        checkEqualsWithoutId(expected, actual);
    }

    private static void checkEqualsWithoutId(Message expected, Message actual) {

    }
}
