package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.model.entity.CommandEntity.Status;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;

import java.util.Optional;
import java.util.Set;

public interface UnitRepository extends JpaRepository<UnitEntity, Long> {
    @EntityGraph(value = "Unit.user_and_device")
    Optional<UnitEntity> findByDevice_imei(String imei);

    @Query(value = "SELECT new org.springframework.data.util.Pair("
            + "u, (SELECT c FROM CommandEntity c WHERE c.device.imei = :imei AND c.status IN (:statuses))"
            + ") "
            + "FROM UnitEntity u WHERE u.device.imei = :imei")
    Pair<UnitEntity, Set<CommandEntity>> findUnitByDeviceImeiWithCommandsWithGivenStatuses(@Param("statuses") Set<Status> statuses);
}
