package by.aurorasoft.signum.crud.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "device_state")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DeviceStateEntity extends BaseEntity<Long> {

    @Id
    @Column(name = "device_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "last_message_id")
    private MessageEntity lastMessage;
}
