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
public class CommandEntity extends BaseEntity {

    @Column(name = "text")
    private String text;

    @Enumerated(STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tracker_id")
    private TrackerEntity tracker;

    public CommandEntity(Long id, String text, Status status, TrackerEntity tracker) {
        super(id);
        this.text = text;
        this.status = status;
        this.tracker = tracker;
    }

    @Override
    public String toString() {
        return super.toString() + "[text = " + this.text + ", status = " + this.status
                + ", trackerId = " + this.tracker.getId() + "]";
    }

    public enum Status {
        NOT_DEFINED, NEW, SENT, SUCCESS_ANSWERED, ERROR_ANSWERED, TIMEOUT_NOT_ANSWERED
    }
}
