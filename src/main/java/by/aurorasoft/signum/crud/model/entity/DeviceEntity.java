package by.aurorasoft.signum.crud.model.entity;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "device")
@SQLDelete(sql = "UPDATE device SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DeviceEntity implements AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "imei")
    private String imei;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(STRING)
    @Column(name = "type")
    private Type type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "unit_id")
    private UnitEntity unit;

    public enum Type {
        NOT_DEFINED, TRACKER, BEACON
    }

    @Override
    public String toString() {
        return super.toString() + "[imei = " + this.imei + ", phoneNumber = " + this.phoneNumber
                + ", type = " + this.type + ", unitId = " + this.unit.getId() + "]";
    }
}
