package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;
import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public final class MessageMapperTest extends AbstractContextTest {

    @Autowired
    private MessageMapper mapper;

    @Test
    public void entityShouldBeMappedToDto() {
        final MessageEntity givenEntity = MessageEntity.builder()
                .id(255L)
                .device(super.entityManager.getReference(DeviceEntity.class, 25551L))
                .dateTime(parse("2000-02-18T04:05:06Z"))
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
                .build();

        final Message actual = this.mapper.toDto(givenEntity);
        final Message expected = new Message(255L, parse("2000-02-18T04:05:06Z"),
                new GpsCoordinate(4.4F, 5.5F), 6, 7, 8, 9,
                Map.of(
                        GSM_LEVEL, 11F,
                        VOLTAGE, 22.2F,
                        CORNER_ACCELERATION, 23.3F,
                        ACCELERATION_UP, 24.4F,
                        ACCELERATION_DOWN, 25.5F)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Message givenDto = new Message(255L, parse("2000-02-18T04:05:06Z"),
                new GpsCoordinate(4.4F, 5.5F), 6, 7, 8, 9,
                Map.of(
                        GSM_LEVEL, 11F,
                        VOLTAGE, 22.2F,
                        CORNER_ACCELERATION, 23.3F,
                        ACCELERATION_UP, 24.4F,
                        ACCELERATION_DOWN, 25.5F)
        );

        final MessageEntity resultEntity = this.mapper.toEntity(25551L, givenDto);

        assertEquals(255L, resultEntity.getId().longValue());
        assertEquals(25551L, resultEntity.getDevice().getId().longValue());
        assertEquals(parse("2000-02-18T04:05:06Z"), resultEntity.getDateTime());
        assertEquals(4.4F, resultEntity.getLatitude(), 0.001);
        assertEquals(5.5F, resultEntity.getLongitude(), 0.001);
        assertEquals(6, resultEntity.getSpeed());
        assertEquals(7, resultEntity.getCourse());
        assertEquals(8, resultEntity.getAltitude());
        assertEquals(9, resultEntity.getAmountSatellite());
        assertEquals(11F, resultEntity.getGsmLevel(), 0.001);
        assertEquals(22.2F, resultEntity.getOnboardVoltage(), 0.001);
        assertEquals(23.3F, resultEntity.getEcoCornering(), 0.001);
        assertEquals(24.4F, resultEntity.getEcoAcceleration(), 0.001);
        assertEquals(25.5F, resultEntity.getEcoBraking(), 0.001);
        assertNull(resultEntity.getType());
    }
}
