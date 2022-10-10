package by.aurorasoft.signum.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

import java.time.Instant;

@Value
public class MessageDto implements AbstractDto<Long> {
    Long id;
    Instant dateTime;
    GpsCoordinate coordinate;
    int speed;
    int course;
    int altitude;
    int amountSatellite;
    float hdop;
    String parameters;

    @Value
    public static class GpsCoordinate {
        float latitude;
        float longitude;
    }
}
