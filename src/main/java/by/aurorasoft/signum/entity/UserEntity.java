package by.aurorasoft.signum.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "user")
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
