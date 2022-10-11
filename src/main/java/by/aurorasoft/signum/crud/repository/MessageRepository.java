package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

}
