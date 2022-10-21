package by.aurorasoft.signum.crud.model.entity;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "app_user")
@SQLDelete(sql = "UPDATE app_user SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class UserEntity extends NamedEntity<Long> {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<UnitEntity> units;

    public UserEntity(String name, List<UnitEntity> units) {
        super(name);
        this.units = units;
    }

    public UserEntity(Long id, String name, List<UnitEntity> units) {
        super(name);
        this.id = id;
        this.units = units;
    }
}
