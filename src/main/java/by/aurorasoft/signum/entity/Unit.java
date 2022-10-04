package by.aurorasoft.signum.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;

@javax.persistence.Entity
@Table(name = "units")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Unit extends Entity {
    private String name;

    @ManyToOne(fetch = LAZY, cascade = { PERSIST, MERGE, REFRESH })
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = LAZY, cascade = { PERSIST, MERGE, REFRESH })
    @JoinColumn(name = "unit_id")
    private Tracker tracker;

    @Override
    public final String toString() {
        return super.toString() + "[name = " + this.getName() + ", userId = " + this.user.getId()
                + ", trackerId = " + this.tracker.getId() + "]";
    }
}
