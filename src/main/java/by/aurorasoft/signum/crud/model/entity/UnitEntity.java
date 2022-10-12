package by.aurorasoft.signum.crud.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.sound.midi.Track;

@Entity
@Table(name = "unit")
@SQLDelete(sql = "UPDATE unit SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class UnitEntity extends NamedEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne
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
