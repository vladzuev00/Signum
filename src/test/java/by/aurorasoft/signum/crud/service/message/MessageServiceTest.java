package by.aurorasoft.signum.crud.service.message;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;
import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.CORRECT;
import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.VALID;
import static java.time.Instant.now;
import static org.junit.Assert.*;

public final class MessageServiceTest extends AbstractContextTest {

    @Autowired
    private MessageService service;

    @Test
    public void messageShouldBeSaved() {
        final Message givenMessage = new Message(now(), new GpsCoordinate(5.5F, 6.6F),
                7, 8, 10, 11,
                Map.of(HDOP, 6F, VDOP, 4F, PDOP, -1F, GSM_LEVEL, 5F, VOLTAGE, 6F));

        final Message savedMessage = this.service.save(25551L, givenMessage);
        final MessageEntity actual = super.findEntityFromDB(MessageEntity.class, savedMessage.getId());
        final MessageEntity expected = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .dateTime(givenMessage.getDateTime())
                .latitude(5.5F)
                .longitude(6.6F)
                .speed(7)
                .course(8)
                .altitude(10)
                .amountSatellite(11)
                .gsmLevel(5)
                .onboardVoltage(6)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .build();
        checkEqualsWithoutId(expected, actual);
    }

    @Test
    public void messagesShouldBeSaved() {
        final List<Message> givenMessages = List.of(
                new Message(now(), new GpsCoordinate(5.5F, 6.6F),
                        7, 8, 10, 11,
                        Map.of(HDOP, 6F, VDOP, 4F, PDOP, -1F, GSM_LEVEL, 5F, VOLTAGE, 6F)),
                new Message(now(), new GpsCoordinate(5.5F, 6.6F),
                        7, 8, 10, 2,
                        Map.of(HDOP, 6F, VDOP, 4F, PDOP, -1F, GSM_LEVEL, 5F, VOLTAGE, 6F))
        );

        this.service.saveAll(25551L, givenMessages);

        final List<MessageEntity> actualMessages = super.findEntities(MessageEntity.class);
        assertEquals(2, actualMessages.size());

        final MessageEntity firstExpectedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .dateTime(givenMessages.get(0).getDateTime())
                .latitude(5.5F)
                .longitude(6.6F)
                .speed(7)
                .course(8)
                .altitude(10)
                .amountSatellite(11)
                .gsmLevel(5)
                .onboardVoltage(6)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(VALID)
                .build();
        checkEqualsWithoutId(firstExpectedMessage, actualMessages.get(0));

        final MessageEntity secondExpectedMessage = MessageEntity.builder()
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .dateTime(givenMessages.get(0).getDateTime())
                .latitude(5.5F)
                .longitude(6.6F)
                .speed(7)
                .course(8)
                .altitude(10)
                .amountSatellite(2)
                .gsmLevel(5)
                .onboardVoltage(6)
                .ecoCornering(-1)
                .ecoAcceleration(-1)
                .ecoBraking(-1)
                .type(CORRECT)
                .build();
        checkEqualsWithoutId(secondExpectedMessage, actualMessages.get(1));
    }

    private static void checkEqualsWithoutId(MessageEntity expected, MessageEntity actual) {
        assertEquals(expected.getDevice().getId(), actual.getDevice().getId());
        assertTrue(Duration.between(expected.getDateTime(), actual.getDateTime()).getSeconds() < 1);
        assertEquals(expected.getLatitude(), actual.getLatitude(), 0.001);
        assertEquals(expected.getLongitude(), actual.getLongitude(), 0.001);
        assertEquals(expected.getSpeed(), actual.getSpeed());
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getAmountSatellite(), actual.getAmountSatellite());
        assertEquals(expected.getGsmLevel(), actual.getGsmLevel());
        assertEquals(expected.getOnboardVoltage(), actual.getOnboardVoltage(), 0.001);
        assertEquals(expected.getEcoCornering(), actual.getEcoCornering(), 0.001);
        assertEquals(expected.getEcoAcceleration(), actual.getEcoAcceleration(), 0.001);
        assertEquals(expected.getEcoBraking(), actual.getEcoBraking(), 0.001);
        assertSame(expected.getType(), actual.getType());
    }
}
