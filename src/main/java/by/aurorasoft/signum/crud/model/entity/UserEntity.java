package by.aurorasoft.signum.crud.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
    private List<UnitEntity> units;
}
