package by.aurorasoft.signum.entity;

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
}
