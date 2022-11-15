package by.aurorasoft.signum.crud.model.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.Getter;
import lombok.Value;

import java.time.Instant;
import java.util.Map;

import static java.util.Map.copyOf;
import static lombok.AccessLevel.NONE;

@Value
public class Message implements AbstractDto<Long> {
    private static final Double NOT_DEFINED_PARAMETER_VALUE = -1.;

    Long id;
    Instant datetime;
    GpsCoordinate coordinate;
    int speed;
    int course;
    int altitude;
    int amountSatellite;
    @Getter(NONE)
    Map<ParameterName, Double> parameterNamesToValues;

    public Message(Instant datetime, GpsCoordinate coordinate, int speed, int course, int altitude,
                   int amountSatellite, Map<ParameterName, Double> parameterNamesToValues) {
        this.id = null;
        this.datetime = datetime;
        this.coordinate = coordinate;
        this.speed = speed;
        this.course = course;
        this.altitude = altitude;
        this.amountSatellite = amountSatellite;
        this.parameterNamesToValues = copyOf(parameterNamesToValues);
    }

    public Message(Long id, Instant datetime, GpsCoordinate coordinate, int speed, int course, int altitude,
                   int amountSatellite, Map<ParameterName, Double> parameterNamesToValues) {
        this.id = id;
        this.datetime = datetime;
        this.coordinate = coordinate;
        this.speed = speed;
        this.course = course;
        this.altitude = altitude;
        this.amountSatellite = amountSatellite;
        this.parameterNamesToValues = copyOf(parameterNamesToValues);
    }

    public Message(Message other, GpsCoordinate coordinate) {
        this.id = other.id;
        this.datetime = other.datetime;
        this.coordinate = coordinate;
        this.speed = other.speed;
        this.course = other.course;
        this.altitude = other.altitude;
        this.amountSatellite = other.amountSatellite;
        this.parameterNamesToValues = copyOf(other.parameterNamesToValues);
    }

    public Double getParameter(ParameterName parameterName) {
        return this.parameterNamesToValues.getOrDefault(parameterName, NOT_DEFINED_PARAMETER_VALUE);
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
        ACCELERATION_DOWN,
        HDOP,
        VDOP,
        PDOP,
        GPS_ODOMETER,
        IGNITION,
        ENGINE_TIME
    }
}
