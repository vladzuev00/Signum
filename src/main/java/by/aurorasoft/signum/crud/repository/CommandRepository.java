package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.model.entity.CommandEntity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface CommandRepository extends JpaRepository<CommandEntity, Long> {
//    @Modifying
//    @Query("UPDATE CommandEntity c SET c.status = :newStatus WHERE c.id = :id")
//    void updateByStatus(@Param("id") Long id, @Param("newStatus") Status newStatus);
//
//    @Query("SELECT c FROM CommandEntity c WHERE c.device.id = :deviceId AND c.status IN (:statuses)")
//    List<CommandEntity> findByDeviceIdAndStatuses(@Param("deviceId") Long deviceId,
//                                                  @Param("statuses") Set<Status> statuses);
}
