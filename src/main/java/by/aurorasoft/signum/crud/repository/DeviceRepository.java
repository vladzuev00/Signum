package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {

    @Query("SELECT e FROM DeviceEntity e WHERE e.imei = :imei")
    Optional<DeviceEntity> findByImei(@Param("imei") String imei);
}
