package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.dto.Message.ParameterName;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;
import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.VALID;
import static java.time.Instant.parse;
import static org.junit.Assert.*;

public final class MessageMapperTest extends AbstractContextTest {

    @Autowired
    private MessageMapper mapper;

    @Test
    public void entityShouldBeMappedToDto() {
        final MessageEntity givenEntity = MessageEntity.builder()
                .id(255L)
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2000-02-18T04:05:06Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(6)
                .course(7)
                .altitude(8)
                .amountSatellite(9)
                .gsmLevel(11)
                .onboardVoltage(22.2F)
                .ecoCornering(23.3F)
                .ecoAcceleration(24.4F)
                .ecoBraking(25.5F)
                .type(VALID)
                .gpsOdometer(26.6)
                .ignition(1)
                .engineTime(500)
                .shock(27.7)
                .build();

        final Message actual = this.mapper.toDto(givenEntity);
        final Message expected = Message.builder()
                .id(255L)
                .datetime(parse("2000-02-18T04:05:06Z"))
                .coordinate(new GpsCoordinate(4.4F, 5.5F))
                .speed(6)
                .course(7)
                .altitude(8)
                .amountSatellite(9)
                .parameterNamesByValues(Map.of(
                        GSM_LEVEL, 11.,
                        VOLTAGE, 22.2,
                        CORNER_ACCELERATION, 23.3,
                        ACCELERATION_UP, 24.4,
                        ACCELERATION_DOWN, 25.5))
                .type(VALID)
                .gpsOdometer(26.6)
                .ignition(1)
                .engineTime(500)
                .shock(27.7)
                .deviceId(25551L)
                .build();

        checkEquals(expected, actual);
    }

    @Test
    public void entityWithNullAsDeviceShouldBeMappedToDtoWithDeviceIdEqualsZero() {
        final MessageEntity givenEntity = MessageEntity.builder()
                .id(255L)
                .datetime(parse("2000-02-18T04:05:06Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(6)
                .course(7)
                .altitude(8)
                .amountSatellite(9)
                .gsmLevel(11)
                .onboardVoltage(22.2F)
                .ecoCornering(23.3F)
                .ecoAcceleration(24.4F)
                .ecoBraking(25.5F)
                .type(VALID)
                .gpsOdometer(26.6)
                .ignition(1)
                .engineTime(500)
                .shock(27.7)
                .build();

        final Message actual = this.mapper.toDto(givenEntity);
        final Message expected = Message.builder()
                .id(255L)
                .datetime(parse("2000-02-18T04:05:06Z"))
                .coordinate(new GpsCoordinate(4.4F, 5.5F))
                .speed(6)
                .course(7)
                .altitude(8)
                .amountSatellite(9)
                .parameterNamesByValues(Map.of(
                        GSM_LEVEL, 11.,
                        VOLTAGE, 22.2,
                        CORNER_ACCELERATION, 23.3,
                        ACCELERATION_UP, 24.4,
                        ACCELERATION_DOWN, 25.5))
                .type(VALID)
                .gpsOdometer(26.6)
                .ignition(1)
                .engineTime(500)
                .shock(27.7)
                .deviceId(0L)
                .build();

        checkEquals(expected, actual);
    }

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Message givenDto = Message.builder()
                .id(255L)
                .datetime(parse("2000-02-18T04:05:06Z"))
                .coordinate(new GpsCoordinate(4.4F, 5.5F))
                .speed(6)
                .course(7)
                .altitude(8)
                .amountSatellite(9)
                .parameterNamesByValues(Map.of(
                        GSM_LEVEL, 11.,
                        VOLTAGE, 22.2,
                        CORNER_ACCELERATION, 23.3,
                        ACCELERATION_UP, 24.4,
                        ACCELERATION_DOWN, 25.5))
                .type(VALID)
                .gpsOdometer(26.6)
                .ignition(1)
                .engineTime(500)
                .shock(27.7)
                .deviceId(25551L)
                .build();

        final MessageEntity actual = this.mapper.toEntity(givenDto);
        final MessageEntity expected = MessageEntity.builder()
                .id(255L)
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .datetime(parse("2000-02-18T04:05:06Z"))
                .latitude(4.4F)
                .longitude(5.5F)
                .speed(6)
                .course(7)
                .altitude(8)
                .amountSatellite(9)
                .gsmLevel(11)
                .onboardVoltage(22.2F)
                .ecoCornering(23.3F)
                .ecoAcceleration(24.4F)
                .ecoBraking(25.5F)
                .type(VALID)
                .gpsOdometer(26.6)
                .ignition(1)
                .engineTime(500)
                .shock(27.7)
                .build();

        checkEquals(expected, actual);
    }

    private static void checkEquals(Message expected, Message actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDatetime(), actual.getDatetime());
        assertEquals(expected.getCoordinate(), actual.getCoordinate());
        assertEquals(expected.getSpeed(), actual.getSpeed());
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getAmountSatellite(), actual.getAmountSatellite());
        checkEqualsParametersWithInaccuracy(
                expected.getParameterNamesByValues(),
                actual.getParameterNamesByValues());
        assertSame(expected.getType(), actual.getType());
        assertEquals(expected.getGpsOdometer(), actual.getGpsOdometer(), 0.);
        assertEquals(expected.getIgnition(), actual.getIgnition());
        assertEquals(expected.getEngineTime(), actual.getEngineTime());
        assertEquals(expected.getShock(), actual.getShock(), 0.);
        assertEquals(expected.getId(), actual.getId());
    }

    private static void checkEqualsParametersWithInaccuracy(Map<ParameterName, Double> expected,
                                                            Map<ParameterName, Double> actual) {
        assertEquals(expected.size(), actual.size());
        expected.forEach((parameterName, parameterValue)
                -> checkEqualsWithInaccuracy(parameterValue, actual.get(parameterName)));
    }

    private static void checkEquals(MessageEntity expected, MessageEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDevice(), actual.getDevice());
        assertEquals(expected.getDatetime(), actual.getDatetime());
        assertEquals(expected.getLatitude(), actual.getLatitude(), 0.);
        assertEquals(expected.getLongitude(), actual.getLongitude(), 0.);
        assertEquals(expected.getSpeed(), actual.getSpeed());
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getAmountSatellite(), actual.getAmountSatellite());
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
