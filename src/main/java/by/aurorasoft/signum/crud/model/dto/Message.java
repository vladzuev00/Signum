package by.aurorasoft.signum.crud.model.dto;

import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.distancecalculator.LatLngAlt;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.Instant;
import java.util.Map;

import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.VALID;
import static java.util.Map.copyOf;

@Data
@Builder
public final class Message implements AbstractDto<Long>, LatLngAlt {
    private static final Double NOT_EXISTING_PARAMETER_VALUE = -1.;

    private Long id;
    private Instant datetime;
    private GpsCoordinate coordinate;
    private int speed;
    private int course;
    private int altitude;
    private int amountSatellite;
    private Map<ParameterName, Double> parameterNamesByValues;
    private MessageType type;
    private double gpsOdometer;
    private int ignition;
    private long engineTime;
    private double shock;
    private Long deviceId;

    public Message(Instant datetime, GpsCoordinate coordinate, int speed, int course, int altitude,
                   int amountSatellite, Map<ParameterName, Double> parameterNamesByValues, MessageType type,
                   double gpsOdometer, int ignition, long engineTime, double shock, Long deviceId) {
        this.id = null;
        this.datetime = datetime;
        this.coordinate = coordinate;
        this.speed = speed;
        this.course = course;
        this.altitude = altitude;
        this.amountSatellite = amountSatellite;
        this.parameterNamesByValues = parameterNamesByValues != null ? copyOf(parameterNamesByValues) : null;
        this.type = type;
        this.gpsOdometer = gpsOdometer;
        this.ignition = ignition;
        this.engineTime = engineTime;
        this.shock = shock;
        this.deviceId = deviceId;
    }

    public Message(Long id, Instant datetime, GpsCoordinate coordinate, int speed, int course, int altitude,
                   int amountSatellite, Map<ParameterName, Double> parameterNamesByValues, MessageType type,
                   double gpsOdometer, int ignition, long engineTime, double shock, Long deviceId) {
        this.id = id;
        this.datetime = datetime;
        this.coordinate = coordinate;
        this.speed = speed;
        this.course = course;
        this.altitude = altitude;
        this.amountSatellite = amountSatellite;
        this.parameterNamesByValues = parameterNamesByValues != null ? copyOf(parameterNamesByValues) : null;
        this.type = type;
        this.gpsOdometer = gpsOdometer;
        this.ignition = ignition;
        this.engineTime = engineTime;
        this.shock = shock;
        this.deviceId = deviceId;
    }

    public Double getParameter(ParameterName name) {
        return this.getParameterOrDefault(name, NOT_EXISTING_PARAMETER_VALUE);
    }

    public Double getParameterOrDefault(ParameterName name, Double defaultValue) {
        return this.parameterNamesByValues != null
                ? this.parameterNamesByValues.getOrDefault(name, defaultValue)
                : defaultValue;
    }

    @Override
    public float getLatitude() {
        return this.coordinate.getLatitude();
    }

    @Override
    public float getLongitude() {
        return this.coordinate.getLongitude();
    }

    @Override
    public boolean isValid() {
        return this.type == VALID;
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
        ACC_X,
        ACC_Y,
        ACC_Z
    }
}
