package by.aurorasoft.signum.crud.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "unit")
@SQLDelete(sql = "UPDATE unit SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UnitEntity extends NamedEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "tracker_id")
    private TrackerEntity tracker;

    @Override
    public String toString() {
        return super.toString() + "[name = " + this.getName() + ", userId = " + this.user.getId()
                + ", tracker = " + this.tracker + "]";
    }
}
