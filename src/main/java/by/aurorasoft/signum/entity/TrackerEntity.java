package by.aurorasoft.signum.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "tracker")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class TrackerEntity extends BaseEntity {

    @Column(name = "imei")
    private String imei;

    @Column(name = "phone_number")
    private String phoneNumber;
}
