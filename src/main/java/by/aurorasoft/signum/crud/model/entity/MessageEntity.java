package by.aurorasoft.signum.crud.model.entity;

import lombok.*;
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

    @Column(name = "hdop")
    private float hdop;

    @Column(name = "gsm_level_percent")
    private byte gsmLevelPercent;

    @Column(name = "voltage")
    private float voltage;

    @Column(name = "corner_acceleration")
    private float cornerAcceleration;

    @Column(name = "acceleration_up")
    private float accelerationUp;

    @Column(name = "acceleration_down")
    private float accelerationDown;

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
                + ", hdop = " + this.hdop
                + ", gsmLevelPercent = " + this.gsmLevelPercent
                + ", voltage = " + this.voltage
                + ", cornerAcceleration = " + this.cornerAcceleration
                + ", accelerationUp = " + this.accelerationUp
                + ", accelerationDown = " + this.accelerationDown
                + "]";
    }
}
