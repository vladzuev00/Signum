package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.VALID;
import static java.lang.Long.MIN_VALUE;
import static java.time.Instant.now;
import static java.time.Instant.parse;
import static org.junit.Assert.*;

public final class MessageRepositoryTest extends AbstractContextTest {
    @Autowired
    private MessageRepository repository;

    @Test
    public void messageShouldBeInserted() {
        final MessageEntity givenMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(now())
                .latitude(5.5F)
                .longitude(6.6F)
                .speed(10)
                .course(11)
                .altitude(12)
                .amountSatellite(13)
                .gsmLevel(54)
                .onboardVoltage(7.7F)
                .ecoCornering(8.8F)
                .ecoAcceleration(9.9F)
                .ecoBraking(10.1F)
                .type(VALID)
                .gpsOdometer(11.1)
                .ignition(12)
                .engineTime(5000)
                .shock(13.3)
                .build();
        super.startQueryCount();
        this.repository.save(givenMessage);
        super.checkQueryCount(1);
    }

    @Test
    @Sql("classpath:sql/insert-message.sql")
    public void messageShouldBeFoundById() {
        super.startQueryCount();
        final MessageEntity actual = this.repository.findById(25551L).orElseThrow();
        super.checkQueryCount(1);

        final MessageEntity expected = MessageEntity.builder()
                .id(25551L)
                .datetime(parse("2000-02-18T04:05:06Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(10)
                .course(11)
                .altitude(12)
                .amountSatellite(13)
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .gsmLevel(44)
                .onboardVoltage(1.5F)
                .ecoCornering(1.6F)
                .ecoAcceleration(1.7F)
                .ecoBraking(1.8F)
                .type(VALID)
                .gpsOdometer(0.1)
                .ignition(1)
                .engineTime(1000)
                .shock(2)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/insert-message.sql")
    public void deviceLastMessageShouldBeFound() {
        final Long givenDeviceId = 25551L;

        super.startQueryCount();
        final Optional<MessageEntity> optionalActual = this.repository.findLastMessage(givenDeviceId);
        super.checkQueryCount(1);
        assertTrue(optionalActual.isPresent());
        final MessageEntity actual = optionalActual.get();

        final MessageEntity expected = MessageEntity.builder()
                .id(25551L)
                .datetime(parse("2000-02-18T04:05:06Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(10)
                .course(11)
                .altitude(12)
                .amountSatellite(13)
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .gsmLevel(44)
                .onboardVoltage(1.5F)
                .ecoCornering(1.6F)
                .ecoAcceleration(1.7F)
                .ecoBraking(1.8F)
                .type(VALID)
                .gpsOdometer(0.1)
                .ignition(1)
                .engineTime(1000)
                .shock(2)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void deviceLastMessageShouldNotBeFound() {
        final Optional<MessageEntity> optionalActual = this.repository.findLastMessage(MIN_VALUE);
        assertTrue(optionalActual.isEmpty());
    }

    private static void checkEquals(MessageEntity expected, MessageEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDatetime(), expected.getDatetime());
        assertEquals(expected.getLatitude(), actual.getLatitude(), 0.);
        assertEquals(expected.getLongitude(), actual.getLongitude(), 0.);
        assertEquals(expected.getSpeed(), actual.getSpeed());
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getDevice().getId(), actual.getDevice().getId());
        assertEquals(expected.getGsmLevel(), actual.getGsmLevel());
        assertEquals(expected.getOnboardVoltage(), actual.getOnboardVoltage(), 0.);
        assertEquals(expected.getEcoCornering(), actual.getEcoCornering(), 0.);
        assertEquals(expected.getEcoAcceleration(), actual.getEcoAcceleration(), 0.);
        assertEquals(expected.getEcoBraking(), actual.getEcoBraking(), 0.);
        assertSame(expected.getType(), actual.getType());
        assertEquals(expected.getGpsOdometer(), actual.getGpsOdometer(), 0.);
        assertEquals(expected.getIgnition(), actual.getIgnition());
        assertEquals(expected.getEngineTime(), actual.getEngineTime());
        assertEquals(expected.getShock(), actual.getShock(), 0.);
    }
}
