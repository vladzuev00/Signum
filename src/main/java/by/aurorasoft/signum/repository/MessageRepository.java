package by.aurorasoft.signum.repository;

import by.aurorasoft.signum.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

}
