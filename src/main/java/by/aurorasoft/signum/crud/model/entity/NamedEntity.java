package by.aurorasoft.signum.crud.model.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public abstract class NamedEntity<IdType> extends BaseEntity<IdType> {

    @Column(name = "name")
    private String name;
}
