package by.aurorasoft.signum.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class Message {
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
