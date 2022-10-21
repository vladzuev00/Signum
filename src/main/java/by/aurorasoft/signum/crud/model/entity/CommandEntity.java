package by.aurorasoft.signum.crud.model.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "command")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CommandEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @Enumerated(STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tracker_id")
    private TrackerEntity tracker;

    @Enumerated(STRING)
    @Column(name = "type")
    private Type type;

    @Override
    public String toString() {
        return  super.toString()
                + "[text = " + this.text
                + ", status = " + this.status
                + ", trackerId = " + this.tracker.getId()
                + ", type = " + this.type + "]";
    }

    public enum Status {
        NOT_DEFINED, NEW, SENT, SUCCESS, ERROR, TIMEOUT
    }

    public enum Type {

        NOT_DEFINED,

        /**
         * indicates that command has been delivered by server to tracker
         */
        COMMAND,

        /**
         * indicates that command has been delivered by tracker to server
         */
        ANSWER
    }
}
