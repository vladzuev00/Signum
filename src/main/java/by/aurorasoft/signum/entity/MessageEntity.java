package by.aurorasoft.signum.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class MessageEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "tracker_id")
    private TrackerEntity tracker;

    @Column(name = "datetime")
    private Instant dateTime;

    @Embedded
    private GpsCoordinate coordinate;

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

    @Column(name = "params")
    private String parameters;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode
    @ToString
    public static final class GpsCoordinate {

        @Column(name = "latitude")
        private float latitude;

        @Column(name = "longitude")
        private float longitude;
    }
}
