package by.aurorasoft.signum.crud.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "message")
@SQLDelete(sql = "UPDATE message SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class MessageEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private UnitEntity unit;

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

    @Column(name = "params")
    private String parameters;
}
