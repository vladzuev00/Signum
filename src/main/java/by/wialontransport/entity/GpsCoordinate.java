package by.wialontransport.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public final class GpsCoordinate {
    private float latitude;
    private float longitude;
}
