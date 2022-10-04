package by.aurorasoft.signum.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "unit")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UnitEntity extends NamedEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "unit_id")
    private TrackerEntity tracker;

    @Override
    public String toString() {
        return super.toString() + "[name = " + this.getName() + ", userId = " + this.user.getId()
                + ", tracker = " + this.tracker + "]";
    }
}
