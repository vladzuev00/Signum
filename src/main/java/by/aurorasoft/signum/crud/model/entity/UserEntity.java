package by.aurorasoft.signum.crud.model.entity;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

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
public class UserEntity extends NamedEntity {

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @Fetch(FetchMode.SUBSELECT)
    private List<UnitEntity> units;

    public UserEntity(String name, List<UnitEntity> units) {
        super(name);
        this.units = units;
    }

    public UserEntity(Long id, String name, List<UnitEntity> units) {
        super(id, name);
        this.units = units;
    }
}
