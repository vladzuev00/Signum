package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<UnitEntity, Long> {
    Optional<UnitEntity> findByTracker_imei(String imei);
}
