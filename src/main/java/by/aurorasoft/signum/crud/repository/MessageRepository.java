package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("SELECT ds.lastMessage FROM DeviceStateEntity ds WHERE ds.id = :deviceId")
    Optional<MessageEntity> findLastMessage(Long deviceId);
}
