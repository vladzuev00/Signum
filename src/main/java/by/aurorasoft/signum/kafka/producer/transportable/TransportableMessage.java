package by.aurorasoft.signum.kafka.producer.transportable;

import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class TransportableMessage {
    Long id;
    long datetimeEpochSecond;
    float latitude;
    float longitude;
    int altitude;
    int speed;
    int amountSatellite;
    int course;
    double gsmLevel;
    double voltage;
    double cornerAcceleration;
    double accelerationUp;
    double accelerationDown;
    MessageType type;
    double gpsOdometer;
    int ignition;
    long engineTime;
    double shock;
    Long deviceId;

    public enum FieldName {
        ID("id"),
        DATE_TIME_EPOCH_SECOND("datetimeEpochSecond"),
        LATITUDE("latitude"),
        LONGITUDE("longitude"),
        ALTITUDE("altitude"),
        SPEED("speed"),
        AMOUNT_SATELLITE("amountSatellite"),
        COURSE("course"),
        GSM_LEVEL("gsmLevel"),
        VOLTAGE("voltage"),
        CORNER_ACCELERATION("cornerAcceleration"),
        ACCELERATION_UP("accelerationUp"),
        ACCELERATION_DOWN("accelerationDown"),
        TYPE("type"),
        GPS_ODOMETER("gpsOdometer"),
        IGNITION("ignition"),
        ENGINE_TIME("engineTime"),
        SHOCK("shock"),
        DEVICE_ID("deviceId");

        private final String name;

        FieldName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
