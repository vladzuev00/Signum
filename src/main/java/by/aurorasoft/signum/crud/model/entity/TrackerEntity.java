package by.aurorasoft.signum.crud.model.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tracker")
@SQLDelete(sql = "UPDATE tracker SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
@Builder
public class TrackerEntity extends BaseEntity {

    @Column(name = "imei")
    private String imei;

    @Column(name = "phone_number")
    private String phoneNumber;

    public TrackerEntity(Long id, String imei, String phoneNumber) {
        super(id);
        this.imei = imei;
        this.phoneNumber = phoneNumber;
    }
}
