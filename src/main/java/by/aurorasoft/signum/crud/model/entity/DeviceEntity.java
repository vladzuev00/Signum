package by.aurorasoft.signum.crud.model.entity;

import by.aurorasoft.signum.crud.model.dto.Device.Type;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "device")
@SQLDelete(sql = "UPDATE device SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
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
    @org.hibernate.annotations.Type(type = "pgsql_enum")
    private Type type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "unit_id")
    private UnitEntity unit;

    @Override
    public String toString() {
        return super.toString() + "[imei = " + this.imei + ", phoneNumber = " + this.phoneNumber
                + ", type = " + this.type + ", unitId = " + this.unit.getId() + "]";
    }
}
