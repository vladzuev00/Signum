package by.aurorasoft.signum.crud.model.entity;

import by.nhorushko.distancecalculator.LatLngAlt;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Instant;

import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.VALID;
import static by.aurorasoft.signum.utils.BoundingUtils.bound;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "message")
@SQLDelete(sql = "UPDATE message SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@EntityListeners(MessageEntity.ParametersBounder.class)
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MessageEntity extends BaseEntity<Long> implements LatLngAlt {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "device_id")
    private DeviceEntity device;

    @Column(name = "time")
    private Instant datetime;

    @Column(name = "latitude")
    private float latitude;

    @Column(name = "longitude")
    private float longitude;

    @Column(name = "speed")
    private int speed;

    @Column(name = "course")
    private int course;

    @Column(name = "altitude")
    private int altitude;

    @Column(name = "amount_satellite")
    private int amountSatellite;

    @Column(name = "gsm_level")
    private int gsmLevel;

    @Column(name = "onboard_voltage")
    @ColumnTransformer(
            read = "onboard_voltage::DECIMAL / 1000",
            write = "? * 1000"
    )
    private float onboardVoltage;

    @Column(name = "eco_cornering")
    @ColumnTransformer(
            read = "eco_cornering::DECIMAL / 1000",
            write = "? * 1000"
    )
    private float ecoCornering;

    @Column(name = "eco_acceleration")
    @ColumnTransformer(
            read = "eco_acceleration::DECIMAL / 1000",
            write = "? * 1000"
    )
    private float ecoAcceleration;

    @Column(name = "eco_braking")
    @ColumnTransformer(
            read = "eco_braking::DECIMAL / 1000",
            write = "? * 1000"
    )
    private float ecoBraking;

    @Enumerated(STRING)
    @Column(name = "type")
    @Type(type = "pgsql_enum")
    private MessageType type;

    @Column(name = "gps_odometer")
    @ColumnTransformer(
            read = "gps_odometer::DECIMAL / 1000",
            write = "? * 1000"
    )
    private double gpsOdometer;

    @Override
    public boolean isValid() {
        return this.type == VALID;
    }

    @Override
    public String toString() {
        return super.toString()
                + "[deviceId = " + this.device.getId()
                + ", datetime = " + this.datetime
                + ", latitude = " + this.latitude
                + ", longitude = " + this.longitude
                + ", speed = " + this.speed
                + ", course = " + this.course
                + ", altitude = " + this.altitude
                + ", amountSatellite = " + this.amountSatellite
                + ", gsmLevel = " + this.gsmLevel
                + ", onboardVoltage = " + this.onboardVoltage
                + ", ecoCornering = " + this.ecoCornering
                + ", ecoAcceleration = " + this.ecoAcceleration
                + ", ecoBraking = " + this.ecoBraking
                + ", type = " + this.type
                + ", gpsOdometer = " + this.gpsOdometer
                + "]";
    }

    public enum MessageType {
        /**
         * Valid coordinates, valid time, valid order
         */
        VALID,

        /**
         * Not valid coordinates, valid time, valid order
         */
        CORRECT,

        /**
         * any coordinates, valid time, not valid order
         */
        WRONG_ORDER,

        /**
         * other cases
         */
        INCORRECT
    }

    static final class ParametersBounder {
        private static final int MIN = -32;
        private static final int MAX = 32;

        public ParametersBounder() {

        }

        @PrePersist
        @PreUpdate
        public void boundParameters(MessageEntity message) {
            message.gsmLevel = bound(message.gsmLevel, MIN, MAX);
            message.onboardVoltage = bound(message.onboardVoltage, (float) MIN, (float) MAX);
            message.ecoCornering = bound(message.ecoCornering, (float) MIN, (float) MAX);
            message.ecoAcceleration = bound(message.ecoAcceleration, (float) MIN, (float) MAX);
            message.ecoBraking = bound(message.ecoBraking, (float) MIN, (float) MAX);
        }
    }
}
