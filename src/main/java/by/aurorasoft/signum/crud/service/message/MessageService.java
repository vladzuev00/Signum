package by.aurorasoft.signum.crud.service.message;

import by.aurorasoft.signum.crud.mapper.MessageMapper;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import by.aurorasoft.signum.crud.repository.MessageRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
@Transactional
public class MessageService extends AbsServiceRUD<Long, MessageEntity, Message, MessageMapper, MessageRepository> {
    private final MessageTypeIdentifier typeIdentifier;
    private final List<MessagePropertyCalculator> propertyCalculators;

    public MessageService(MessageMapper mapper, MessageRepository repository, MessageTypeIdentifier typeIdentifier,
                          List<MessagePropertyCalculator> propertyCalculators) {
        super(mapper, repository);
        this.typeIdentifier = typeIdentifier;
        this.propertyCalculators = propertyCalculators;
    }

    public Optional<Message> findLastMessage(Long deviceId) {
        final Optional<MessageEntity> optionalEntity = super.repository.findLastMessage(deviceId);
        return optionalEntity.map(super.mapper::toDto);
    }

    /**
     * @return optional of new last valid message, empty when in 'savedMessages' all messages is incorrect.
     */
    public Optional<Message> saveAll(Long deviceId, List<Message> savedMessages) {
        final List<MessageEntity> typedEntities = this.mapToTypedEntities(deviceId, savedMessages);
        final Optional<MessageEntity> optionalNewLastEntity = this.fixAndCalculateProperties(typedEntities);
        super.repository.saveAll(typedEntities);
        return optionalNewLastEntity.map(super.mapper::toDto);
    }

    /**
     * @return optional of new last valid message, empty when in 'savedMessages' all messages is incorrect.
     */
    public Optional<Message> saveAll(Long deviceId, Message last, List<Message> saved) {
        final MessageEntity lastEntity = super.mapper.toEntity(deviceId, last);
        final List<MessageEntity> typedEntities = this.mapToTypedEntities(deviceId, last, saved);
        final Optional<MessageEntity> optionalNewLastEntity
                = this.fixAndCalculateProperties(lastEntity, typedEntities);
        super.repository.saveAll(typedEntities);
        return optionalNewLastEntity.map(super.mapper::toDto);
    }

    private List<MessageEntity> mapToTypedEntities(Long deviceId, List<Message> messages) {
        final List<MessageEntity> typedEntities = new ArrayList<>();
        Message lastValid = null;
        MessageEntity currentMapped;
        for (final Message message : messages) {
            currentMapped = lastValid != null
                    ? this.mapToTypedEntity(deviceId, lastValid, message)
                    : this.mapToTypedEntity(deviceId, message);
            if (currentMapped.getType() == VALID) {
                lastValid = message;
            }
            typedEntities.add(currentMapped);
        }
        return typedEntities;
    }

    private List<MessageEntity> mapToTypedEntities(Long deviceId, Message last, List<Message> messages) {
        final List<MessageEntity> typedEntities = new ArrayList<>();
        MessageEntity currentMapped;
        for (final Message message : messages) {
            currentMapped = this.mapToTypedEntity(deviceId, last, message);
            if (currentMapped.getType() == VALID) {
                last = message;
            }
            typedEntities.add(currentMapped);
        }
        return typedEntities;
    }

    private MessageEntity mapToTypedEntity(Long deviceId, Message message) {
        final MessageType type = this.typeIdentifier.identify(message);
        final MessageEntity typedEntity = super.mapper.toEntity(deviceId, message);
        typedEntity.setType(type);
        return typedEntity;
    }

    private MessageEntity mapToTypedEntity(Long deviceId, Message last, Message message) {
        final MessageType type = this.typeIdentifier.identify(message, last);
        final MessageEntity typedEntity = super.mapper.toEntity(deviceId, message);
        typedEntity.setType(type);
        return typedEntity;
    }

    private Optional<MessageEntity> fixAndCalculateProperties(List<MessageEntity> entities) {
        MessageEntity last = null;
        for (final MessageEntity entity : entities) {
            if (entity.getType() == VALID) {
                last = entity;
            } else if (entity.getType() == CORRECT) {
                if (last == null) {
                    entity.setType(INCORRECT);
                } else {
                    fixCorrectMessage(entity, last);
                    last = entity;
                }
            }
            if (last != null) {
                this.calculateProperties(last, entity);
            } else {
                this.calculateProperties(entity);
            }
        }
        return Optional.ofNullable(last);
    }

    /**
     * Fixes 'CORRECT' messages to 'VALID' and calculates additional properties for messages.
     * @return optional of new last valid message. Empty when all messages is 'INCORRECT'.
     */
    private Optional<MessageEntity> fixAndCalculateProperties(MessageEntity last, List<MessageEntity> entities) {
        MessageEntity newLast = last;
        for (final MessageEntity entity : entities) {
            if (entity.getType() == VALID) {
                newLast = entity;
            } else if (entity.getType() == CORRECT) {
                fixCorrectMessage(entity, newLast);
                newLast = entity;
            }
            this.calculateProperties(newLast, entity);
        }
        return newLast != last ? of(newLast) : empty();
    }

    private static void fixCorrectMessage(MessageEntity fixed, MessageEntity last) {
        fixed.setLatitude(last.getLatitude());
        fixed.setLongitude(last.getLongitude());
        fixed.setType(VALID);
    }

    private void calculateProperties(MessageEntity current) {
        this.propertyCalculators.forEach(calculator -> calculator.calculate(current));
    }

    private void calculateProperties(MessageEntity last, MessageEntity current) {
        this.propertyCalculators.forEach(calculator -> calculator.calculate(last, current));
    }
}
