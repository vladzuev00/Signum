package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface CommandRepository extends JpaRepository<CommandEntity, Long> {
    @Modifying
    @Query("UPDATE CommandEntity c SET c.status = :newStatus WHERE c.id = :id")
    void updateStatus(Long id, Command.Status newStatus);

    @Query("SELECT c FROM CommandEntity c WHERE c.device.id = :deviceId AND c.status IN (:statuses)")
    List<CommandEntity> findByDeviceIdAndStatuses(Long deviceId, Set<Command.Status> statuses);
}
