package by.aurorasoft.signum.crud.model.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

import java.time.Instant;

@Value
public class Message implements AbstractDto<Long> {
    Long id;
    Instant dateTime;
    GpsCoordinate coordinate;
    int speed;
    int course;
    int altitude;
    int amountSatellite;
    float hdop;
    String parameters;

    public Message(Instant dateTime, GpsCoordinate coordinate, int speed, int course, int altitude,
                   int amountSatellite, float hdop, String parameters) {
        this.id = null;
        this.dateTime = dateTime;
        this.coordinate = coordinate;
        this.speed = speed;
        this.course = course;
        this.altitude = altitude;
        this.amountSatellite = amountSatellite;
        this.hdop = hdop;
        this.parameters = parameters;
    }

    public Message(Long id, Instant dateTime, GpsCoordinate coordinate, int speed, int course, int altitude,
                   int amountSatellite, float hdop, String parameters) {
        this.id = id;
        this.dateTime = dateTime;
        this.coordinate = coordinate;
        this.speed = speed;
        this.course = course;
        this.altitude = altitude;
        this.amountSatellite = amountSatellite;
        this.hdop = hdop;
        this.parameters = parameters;
    }

    @Value
    public static class GpsCoordinate {
        float latitude;
        float longitude;
    }
}
