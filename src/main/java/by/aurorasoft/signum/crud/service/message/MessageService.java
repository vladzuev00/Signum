package by.aurorasoft.signum.crud.service.message;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.mapper.MessageMapper;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import by.aurorasoft.signum.crud.repository.MessageRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceExtCRUD;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class MessageService extends AbsServiceExtCRUD<Long, MessageEntity, Message, Long, DeviceEntity, MessageRepository> {
    private final MessageValidator messageValidator;

    public MessageService(MessageMapper mapper, MessageRepository repository, MessageValidator messageValidator) {
        super(mapper, repository);
        this.messageValidator = messageValidator;
    }

    @Override
    public Message save(Long deviceId, Message message) {
        final MessageEntity entityToBeSaved = this.mapToValidatedEntity(deviceId, message);
        final MessageEntity savedEntity = super.repository.save(entityToBeSaved);
        return super.mapper.toDto(savedEntity);
    }

    @Override
    public List<Message> saveAll(Long deviceId, Collection<Message> messages) {
        final List<MessageEntity> entitiesToBeSaved = messages.stream()
                .map(message -> this.mapToValidatedEntity(deviceId, message))
                .collect(toList());
        final List<MessageEntity> savedEntities = super.repository.saveAll(entitiesToBeSaved);
        return super.mapper.toDtos(savedEntities);
    }

    private MessageEntity mapToValidatedEntity(Long deviceId, Message message) {
        final MessageType messageType = this.messageValidator.validate(message);
        final MessageEntity validatedEntity = super.mapper.toEntity(deviceId, message);
        validatedEntity.setType(messageType);
        return validatedEntity;
    }
}
