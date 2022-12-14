package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.dto.Message.ParameterName;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;
import java.util.Optional;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;
import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.VALID;
import static java.time.Instant.parse;
import static java.util.Arrays.stream;
import static org.junit.Assert.*;

public final class MessageServiceTest extends AbstractContextTest {

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
                .parameterNamesByValues(
                        Map.of(GSM_LEVEL, 44., VOLTAGE, 1.5, CORNER_ACCELERATION, 1.6,
                                ACCELERATION_UP, 1.7, ACCELERATION_DOWN, 1.8))
                .type(VALID)
                .gpsOdometer(0.1)
                .ignition(1)
                .engineTime(1000)
                .shock(2)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void lastMessageShouldNotBeFound() {
        final Long givenDeviceId = 25551L;
        final Optional<Message> optionalActual = this.service.findLastMessage(givenDeviceId);
        assertTrue(optionalActual.isEmpty());
    }

    private static void checkEquals(Message expected, Message actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDatetime(), actual.getDatetime());
        assertEquals(expected.getCoordinate(), actual.getCoordinate());
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getAmountSatellite(), actual.getAmountSatellite());
        checkEqualsParameters(expected.getParameterNamesByValues(), actual.getParameterNamesByValues());
        assertSame(expected.getType(), actual.getType());
        checkEqualsWithInaccuracy(expected.getGpsOdometer(), actual.getGpsOdometer());
        assertEquals(expected.getIgnition(), actual.getIgnition());
        assertEquals(expected.getEngineTime(), actual.getEngineTime());
        checkEqualsWithInaccuracy(expected.getShock(), actual.getShock());
    }

    private static void checkEqualsParameters(Map<ParameterName, Double> expected, Map<ParameterName, Double> actual) {
        stream(ParameterName.values())
                .forEach(parameterName
                        -> {
                    final boolean bothExist = expected.containsKey(parameterName) && actual.containsKey(parameterName);
                    final boolean bothNotExist = !expected.containsKey(parameterName)
                            && !actual.containsKey(parameterName);
                    if (bothExist) {
                        checkEqualsWithInaccuracy(expected.get(parameterName), actual.get(parameterName));
                    } else if (bothNotExist) {
                        assertTrue(true);
                    } else {
                        fail();
                    }
                });
    }
}
