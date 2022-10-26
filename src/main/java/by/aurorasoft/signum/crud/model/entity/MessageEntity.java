package by.aurorasoft.signum.crud.model.entity;

import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "message")
@SQLDelete(sql = "UPDATE message SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MessageEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "device_id")
    private DeviceEntity device;

    @Column(name = "time")
    private Instant dateTime;

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

    @Override
    public String toString() {
        return super.toString()
                + "[deviceId = " + this.device.getId()
                + ", dateTime = " + this.dateTime
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
                + "]";
    }
}
