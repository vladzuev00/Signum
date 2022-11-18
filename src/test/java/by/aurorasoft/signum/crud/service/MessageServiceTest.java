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
        assertEquals(expected, actual);
    }

    private static void checkEqualsWithoutId(MessageEntity expected, MessageEntity actual) {
        assertEquals(expected.getDevice().getId(), actual.getDevice().getId());
        assertTrue(Duration.between(expected.getDatetime(), actual.getDatetime()).getSeconds() < 1);
        assertEquals(expected.getLatitude(), actual.getLatitude(), ALLOWABLE_INACCURACY);
        assertEquals(expected.getLongitude(), actual.getLongitude(), ALLOWABLE_INACCURACY);
        assertEquals(expected.getSpeed(), actual.getSpeed());
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getAmountSatellite(), actual.getAmountSatellite());
        assertEquals(expected.getGsmLevel(), actual.getGsmLevel());
        assertEquals(expected.getOnboardVoltage(), actual.getOnboardVoltage(), ALLOWABLE_INACCURACY);
        assertEquals(expected.getEcoCornering(), actual.getEcoCornering(), ALLOWABLE_INACCURACY);
        assertEquals(expected.getEcoAcceleration(), actual.getEcoAcceleration(), ALLOWABLE_INACCURACY);
        assertEquals(expected.getEcoBraking(), actual.getEcoBraking(), ALLOWABLE_INACCURACY);
        assertSame(expected.getType(), actual.getType());
        assertEquals(expected.getGpsOdometer(), actual.getGpsOdometer(), 0.);
        assertEquals(expected.getIgnition(), actual.getIgnition());
        assertEquals(expected.getEngineTime(), actual.getEngineTime());
        assertEquals(expected.getShock(), actual.getShock(), 0.);
    }
}
