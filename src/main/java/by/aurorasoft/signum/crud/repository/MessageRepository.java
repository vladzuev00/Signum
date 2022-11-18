package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query(value = "SELECT id, time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "message.device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type, "
            + "gps_odometer, ignition, engine_time, shock "
            + "FROM message INNER JOIN device_state ON message.id = device_state.last_message_id "
            + "WHERE device_state.device_id = :deviceId", nativeQuery = true)
    Optional<MessageEntity> findLastMessage(Long deviceId);
}
