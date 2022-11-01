package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {
    Optional<DeviceEntity> findByImei(String imei);
}
