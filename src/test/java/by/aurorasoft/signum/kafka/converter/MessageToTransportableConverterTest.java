package by.aurorasoft.signum.kafka.converter;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.kafka.producer.converter.MessageToTransportableConverter;
import by.aurorasoft.signum.kafka.producer.transportable.TransportableMessage;
import org.junit.Test;

import java.util.Map;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;
import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.VALID;
import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;

public final class MessageToTransportableConverterTest {

    private final MessageToTransportableConverter converter;

    public MessageToTransportableConverterTest() {
        this.converter = new MessageToTransportableConverter();
    }

    @Test
    public void messageShouldBeConvertedToTransportable() {
        final Message givenMessage = Message.builder()
                .id(255L)
                .datetime(parse("2007-12-03T10:15:30Z"))
                .coordinate(new GpsCoordinate(4.4F, 5.5F))
                .speed(6)
                .course(7)
                .altitude(8)
                .amountSatellite(9)
                .parameterNamesByValues(Map.of(
                        GSM_LEVEL, 10.,
                        VOLTAGE, 11.,
                        CORNER_ACCELERATION, 12.,
                        ACCELERATION_UP, 13.,
                        ACCELERATION_DOWN, 14.
                ))
                .type(VALID)
                .gpsOdometer(15.)
                .ignition(1)
                .engineTime(500)
                .shock(14.)
                .deviceId(256L)
                .build();

        final TransportableMessage actual = this.converter.convert(givenMessage);
        final TransportableMessage expected = TransportableMessage.builder()
                .id(255L)
                .datetimeEpochSecond(1196676930)
                .latitude(4.4F)
                .longitude(5.5F)
                .altitude(8)
                .speed(6)
                .amountSatellite(9)
                .course(7)
                .gsmLevel(10)
                .voltage(11)
                .cornerAcceleration(12)
                .accelerationUp(13)
                .accelerationDown(14)
                .type(VALID)
                .gpsOdometer(15)
                .ignition(1)
                .engineTime(500)
                .shock(14.)
                .deviceId(256L)
                .build();

        assertEquals(expected, actual);
    }
}
