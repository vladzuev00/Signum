package by.aurorasoft.signum.repository;

import by.aurorasoft.signum.entity.TrackerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TrackerRepository extends CrudRepository<TrackerEntity, Long> {
    Optional<TrackerEntity> findByImei(String imei);
}
