package by.aurorasoft.signum.crud.model.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public abstract class NamedEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    public NamedEntity(Long id, String name) {
        super(id);
        this.name = name;
    }
}
