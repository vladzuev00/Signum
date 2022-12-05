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

//    @Test
//    public void entityShouldBeMappedToDto() {
//        final MessageEntity givenEntity = MessageEntity.builder()
//                .id(255L)
//                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
//                .datetime(parse("2000-02-18T04:05:06Z"))
//                .latitude(4.4F)
//                .longitude(5.5F)
//                .speed(6)
//                .course(7)
//                .altitude(8)
//                .amountSatellite(9)
//                .gsmLevel(11)
//                .onboardVoltage(22.2F)
//                .ecoCornering(23.3F)
//                .ecoAcceleration(24.4F)
//                .ecoBraking(25.5F)
//                .type(VALID)
//                .gpsOdometer(26.6)
//                .ignition(1)
//                .engineTime(500)
//                .shock(27.7)
//                .build();
//
//        final Message actual = this.mapper.toDto(givenEntity);
//        final Message expected = new Message(255L, parse("2000-02-18T04:05:06Z"),
//                new GpsCoordinate(4.4F, 5.5F), 6, 7, 8, 9,
//                Map.of(
//                        GSM_LEVEL, 11.,
//                        VOLTAGE, 22.2,
//                        CORNER_ACCELERATION, 23.3,
//                        ACCELERATION_UP, 24.4,
//                        ACCELERATION_DOWN, 25.5),
//                VALID, 26.6, 1, 500, 27.7
//        );
//        checkEqualsWithParameterInaccuracy(expected, actual);
//    }
//
//    @Test
//    public void dtoShouldBeMappedToEntity() {
//        final Message givenDto = new Message(255L, parse("2000-02-18T04:05:06Z"),
//                new GpsCoordinate(4.4F, 5.5F), 6, 7, 8, 9,
//                Map.of(
//                        GSM_LEVEL, 11.,
//                        VOLTAGE, 22.2,
//                        CORNER_ACCELERATION, 23.3,
//                        ACCELERATION_UP, 24.4,
//                        ACCELERATION_DOWN, 25.5),
//                VALID, 26.6, 1, 500, 27.7
//        );
//
//        final MessageEntity resultEntity = this.mapper.toEntity(25551L, givenDto);
//
//        assertEquals(255L, resultEntity.getId().longValue());
//        assertEquals(25551L, resultEntity.getDevice().getId().longValue());
//        assertEquals(parse("2000-02-18T04:05:06Z"), resultEntity.getDatetime());
//
////        assertEquals(4.4F, resultEntity.getLatitude(), ALLOWABLE_INACCURACY);
////        assertTrue(areEqualsWithInaccuracy());
////
////        assertEquals(5.5F, resultEntity.getLongitude(), ALLOWABLE_INACCURACY);
////        assertEquals(6, resultEntity.getSpeed());
////        assertEquals(7, resultEntity.getCourse());
////        assertEquals(8, resultEntity.getAltitude());
////        assertEquals(9, resultEntity.getAmountSatellite());
////        assertEquals(11F, resultEntity.getGsmLevel(), ALLOWABLE_INACCURACY);
////        assertEquals(22.2F, resultEntity.getOnboardVoltage(), ALLOWABLE_INACCURACY);
////        assertEquals(23.3F, resultEntity.getEcoCornering(), ALLOWABLE_INACCURACY);
////        assertEquals(24.4F, resultEntity.getEcoAcceleration(), ALLOWABLE_INACCURACY);
////        assertEquals(25.5F, resultEntity.getEcoBraking(), ALLOWABLE_INACCURACY);
////        assertSame(VALID, resultEntity.getType());
////        assertEquals(26.6, resultEntity.getGpsOdometer(), ALLOWABLE_INACCURACY);
////        assertEquals(1, resultEntity.getIgnition());
////        assertEquals(500, resultEntity.getEngineTime());
////        assertEquals(27.7, resultEntity.getShock(), ALLOWABLE_INACCURACY);
//    }
//
//    private static void checkEqualsWithParameterInaccuracy(Message expected, Message actual) {
//        assertEquals(expected.getId(), actual.getId());
//        assertEquals(expected.getDatetime(), actual.getDatetime());
//        assertEquals(expected.getCoordinate(), actual.getCoordinate());
//        assertEquals(expected.getSpeed(), actual.getSpeed());
//        assertEquals(expected.getCourse(), actual.getCourse());
//        assertEquals(expected.getAltitude(), actual.getAltitude());
//        assertEquals(expected.getAmountSatellite(), actual.getAmountSatellite());
//        checkEqualsParametersWithInaccuracy(
//                expected.getParameterNamesByValues(),
//                actual.getParameterNamesByValues());
//        assertSame(expected.getType(), actual.getType());
//        assertEquals(expected.getGpsOdometer(), actual.getGpsOdometer(), 0.);
//        assertEquals(expected.getIgnition(), actual.getIgnition());
//        assertEquals(expected.getEngineTime(), actual.getEngineTime());
//        assertEquals(expected.getShock(), actual.getShock(), 0.);
//    }
//
//    private static void checkEqualsParametersWithInaccuracy(Map<ParameterName, Double> expected,
//                                                            Map<ParameterName, Double> actual) {
////        assertEquals(expected.size(), actual.size());
////        expected.forEach((parameterName, parameterValue) -> {
////            assertEquals(parameterValue, actual.get(parameterName), ALLOWABLE_INACCURACY);
////        });
//    }
}
