package by.aurorasoft.signum.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class User extends Entity {

    @Column(name = "name")
    private String name;
}
