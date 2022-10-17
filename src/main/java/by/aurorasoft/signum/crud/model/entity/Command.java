package by.aurorasoft.signum.crud.model.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "command")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Command extends BaseEntity {

    @Column(name = "text")
    private String text;

    @Enumerated(STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tracker_id")
    private TrackerEntity tracker;

    public enum Status {
        DELIVERED, NOT_DELIVERED
    }

    @Override
    public String toString() {
        return super.toString() + "[text = " + this.text + ", status = " + this.status
                + ", trackerId = " + this.tracker.getId() + "]";
    }
}
