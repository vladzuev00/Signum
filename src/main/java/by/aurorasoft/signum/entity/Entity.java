package by.aurorasoft.signum.entity;

import lombok.*;

import javax.persistence.*;

import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public abstract class Entity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "deleted")
    private boolean deleted;

    @Override
    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (this.getClass() != otherObject.getClass()) {
            return false;
        }
        final Entity other = (Entity) otherObject;
        return Objects.equals(this.getId(), other.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(this.getId());
    }
}
