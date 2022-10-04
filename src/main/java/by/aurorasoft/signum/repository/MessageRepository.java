package by.aurorasoft.signum.repository;

import by.aurorasoft.signum.entity.MessageEntity;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<MessageEntity, Long> {

}
