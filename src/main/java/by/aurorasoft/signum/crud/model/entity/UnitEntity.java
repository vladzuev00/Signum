package by.aurorasoft.signum.crud.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

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
@Builder
public class UnitEntity extends NamedEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

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
        super(name);
        this.id = id;
        this.user = user;
        this.tracker = tracker;
    }

    @Override
    public String toString() {
        return super.toString() + "[userId = " + this.user.getId() + ", trackerId = " + this.tracker.getId() + "]";
    }
}
