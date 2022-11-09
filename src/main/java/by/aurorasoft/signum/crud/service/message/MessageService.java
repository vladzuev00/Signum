package by.aurorasoft.signum.crud.service.message;

import by.aurorasoft.signum.crud.mapper.MessageMapper;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import by.aurorasoft.signum.crud.repository.MessageRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
@Transactional
public class MessageService
        extends AbsServiceRUD<Long, MessageEntity, Message, MessageMapper, MessageRepository> {
    private final MessageTypeIdentifier messageValidator;
    private final List<MessagePropertyCalculator> propertyCalculators;

    public MessageService(MessageMapper mapper, MessageRepository repository, MessageTypeIdentifier messageValidator,
                          List<MessagePropertyCalculator> propertyCalculators) {
        super(mapper, repository);
        this.messageValidator = messageValidator;
        this.propertyCalculators = propertyCalculators;
    }

    /**
     * @return optional of new last valid message, empty when in 'savedMessages' all messages is incorrect.
     */
    public Optional<Message> saveAll(Long deviceId, Message last, List<Message> saved) {
        final MessageEntity lastEntity = super.mapper.toEntity(deviceId, last);
        final List<MessageEntity> validatedEntities = this.mapToValidatedEntities(deviceId, last, saved);
        final Optional<MessageEntity> optionalNewLastEntity
                = this.fixAndCalculateProperties(lastEntity, validatedEntities);
        return optionalNewLastEntity.map(super.mapper::toDto);
    }

    /**
     * @return optional of new last valid message, empty when in 'savedMessages' all messages is incorrect.
     */
    public Optional<Message> saveAll(Long deviceId, List<Message> savedMessages) {
        return empty();
    }

    public Optional<Message> findLastReceivedMessage(Long deviceId) {
        final Optional<MessageEntity> optionalEntity = super.repository
                .findLastReceivedMessage(deviceId);
        return optionalEntity.map(super.mapper::toDto);
    }

    private List<MessageEntity> mapToValidatedEntities(Long deviceId, Message last, List<Message> messages) {
        MessageEntity currentMapped;
        for (final Message message : messages) {
            currentMapped = super.mapper.toEntity(deviceId, message);

        }
    }

    private MessageEntity mapToValidatedEntity(Long deviceId, Message message, Message lastValidMessage) {
        final MessageType messageType = this.messageValidator.validate(message, lastValidMessage);
        final MessageEntity validatedEntity = super.mapper.toEntity(deviceId, message);
        validatedEntity.setType(messageType);
        return validatedEntity;
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
            }
            this.calculateProperties(newLast, entity);
        }
        return newLast != last ? of(newLast) : empty();
    }

    private static void fixCorrectMessage(MessageEntity fixedMessage, MessageEntity lastValidMessage) {
        fixedMessage.setLatitude(lastValidMessage.getLatitude());
        fixedMessage.setLongitude(lastValidMessage.getLongitude());
        fixedMessage.setType(VALID);
    }

    private void calculateProperties(MessageEntity last, MessageEntity current) {
        this.propertyCalculators.forEach(calculator -> calculator.calculate(last, current));
    }

    private MessageEntity mapToValidatedEntity(Long deviceId, Message message) {
        final MessageType messageType = this.messageValidator.validate(message);
        final MessageEntity entity = super.mapper.toEntity(deviceId, message);
        entity.setType(messageType);
        return entity;
    }
}
