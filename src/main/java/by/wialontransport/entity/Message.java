package by.wialontransport.entity;

import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Message {
    private Long id;
    private Instant dateTime;
    private GpsCoordinate coordinate;
    private int speed;
    private int course;
    private int altitude;
    private int amountSatellite;
    private float hdop;
    private String parameters;
}
