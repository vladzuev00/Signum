package by.aurorasoft.signum.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class Tracker extends Entity {

    @Column(name = "imei")
    private String imei;

    @Column(name = "phoneNumber")
    private String phoneNumber;
}
