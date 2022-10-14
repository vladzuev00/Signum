package by.aurorasoft.signum.crud.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "unit")
@SQLDelete(sql = "UPDATE unit SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@NamedEntityGraph(
        name = "Unit.user_and_tracker",
        attributeNodes = {
                @NamedAttributeNode(value = "user"),
                @NamedAttributeNode(value = "tracker")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class UnitEntity extends NamedEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "tracker_id")
    private TrackerEntity tracker;

    public UnitEntity(String name, UserEntity user, TrackerEntity tracker) {
        super(name);
        this.user = user;
        this.tracker = tracker;
    }

    public UnitEntity(Long id, String name, UserEntity user, TrackerEntity tracker) {
        super(id, name);
        this.user = user;
        this.tracker = tracker;
    }
}
