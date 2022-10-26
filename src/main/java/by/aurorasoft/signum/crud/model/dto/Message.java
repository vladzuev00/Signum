package by.aurorasoft.signum.crud.model.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Value;

import java.time.Instant;
import java.util.Map;

@Value
public class Message implements AbstractDto<Long> {
    Long id;
    Instant dateTime;
    GpsCoordinate coordinate;
    int speed;
    int course;
    int altitude;
    int amountSatellite;
    Map<ParameterName, Float> parameterNamesToValues;

    public Message(Instant dateTime, GpsCoordinate coordinate, int speed, int course, int altitude,
                   int amountSatellite, Map<ParameterName, Float> parameterNamesToValues) {
        this.id = null;
        this.dateTime = dateTime;
        this.coordinate = coordinate;
        this.speed = speed;
        this.course = course;
        this.altitude = altitude;
        this.amountSatellite = amountSatellite;
        this.parameterNamesToValues = parameterNamesToValues;
    }

    public Message(Long id, Instant dateTime, GpsCoordinate coordinate, int speed, int course, int altitude,
                   int amountSatellite, Map<ParameterName, Float> parameterNamesToValues) {
        this.id = id;
        this.dateTime = dateTime;
        this.coordinate = coordinate;
        this.speed = speed;
        this.course = course;
        this.altitude = altitude;
        this.amountSatellite = amountSatellite;
        this.parameterNamesToValues = parameterNamesToValues;
    }

    @Value
    public static class GpsCoordinate {
        float latitude;
        float longitude;
    }

    public enum ParameterName {
        GSM_LEVEL,
        VOLTAGE,
        CORNER_ACCELERATION,
        ACCELERATION_UP,
        ACCELERATION_DOWN
    }
}
