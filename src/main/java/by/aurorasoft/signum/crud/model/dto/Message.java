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
    float hdop;
    Map<ParameterType, Float> parameterTypesToValues;

    public Message(Instant dateTime, GpsCoordinate coordinate, int speed, int course, int altitude,
                   int amountSatellite, float hdop, Map<ParameterType, Float> parameterTypesToValues) {
        this.id = null;
        this.dateTime = dateTime;
        this.coordinate = coordinate;
        this.speed = speed;
        this.course = course;
        this.altitude = altitude;
        this.amountSatellite = amountSatellite;
        this.hdop = hdop;
        this.parameterTypesToValues = parameterTypesToValues;
    }

    public Message(Long id, Instant dateTime, GpsCoordinate coordinate, int speed, int course, int altitude,
                   int amountSatellite, float hdop, Map<ParameterType, Float> parameterTypesToValues) {
        this.id = id;
        this.dateTime = dateTime;
        this.coordinate = coordinate;
        this.speed = speed;
        this.course = course;
        this.altitude = altitude;
        this.amountSatellite = amountSatellite;
        this.hdop = hdop;
        this.parameterTypesToValues = parameterTypesToValues;
    }

    @Value
    public static class GpsCoordinate {
        float latitude;
        float longitude;
    }

    public enum ParameterType {
        NOT_DEFINED("not defined"),
        GSM_LEVEL("GSMCSQ"),
        VOLTAGE("VPWR"),
        CORNER_ACCELERATION("wln_crn_max"),
        ACCELERATION_UP("wln_accel_max"),
        ACCELERATION_DOWN("wln_brk_max");

        private final String name;

        ParameterType(String name) {
            this.name = name;
        }

        public final String getName() {
            return this.name;
        }
    }
}
