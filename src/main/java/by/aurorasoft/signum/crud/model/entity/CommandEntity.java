package by.aurorasoft.signum.crud.model.entity;

import by.aurorasoft.signum.crud.model.dto.Command.Status;
import by.aurorasoft.signum.crud.model.dto.Command.Type;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "command")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CommandEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @Enumerated(STRING)
    @Column(name = "status")
    @org.hibernate.annotations.Type(type = "pgsql_enum")
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "device_id")
    private DeviceEntity device;

    @Enumerated(STRING)
    @Column(name = "type")
    @org.hibernate.annotations.Type(type = "pgsql_enum")
    private Type type;

    @Override
    public String toString() {
        return  super.toString()
                + "[text = " + this.text
                + ", status = " + this.status
                + ", deviceId = " + this.device.getId()
                + ", type = " + this.type + "]";
    }
}
