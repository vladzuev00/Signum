package by.aurorasoft.signum.crud.service.message;

import by.aurorasoft.signum.crud.mapper.MessageMapper;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import by.aurorasoft.signum.crud.repository.MessageRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import by.nhorushko.distancecalculator.DistanceCalculator;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.*;
import static java.util.Optional.empty;

@Service
@Transactional
public class MessageService
        extends AbsServiceRUD<Long, MessageEntity, Message, MessageMapper, MessageRepository> {
    private final MessageValidator messageValidator;
    private final DistanceCalculatorSettings distanceCalculatorSettings;
    private final DistanceCalculator distanceCalculator;

    public MessageService(MessageMapper mapper, MessageRepository repository, MessageValidator messageValidator,
                          DistanceCalculatorSettings distanceCalculatorSettings,
                          DistanceCalculator distanceCalculator) {
        super(mapper, repository);
        this.messageValidator = messageValidator;
        this.distanceCalculatorSettings = distanceCalculatorSettings;
        this.distanceCalculator = distanceCalculator;
    }

    /**
     * @return optional of new last valid message, empty when in 'savedMessages' all messages is incorrect.
     */
    public Optional<Message> saveAll(Long deviceId, Message lastValidMessage,
                                     List<Message> savedMessages) {
        final Pair<Optional<Message>, List<MessageEntity>> newLastValidMessageAndFixedMessages
                = this.findNewLastMessageAndFixedMessages(deviceId, lastValidMessage, savedMessages);
        super.repository.saveAll(newLastValidMessageAndFixedMessages.getSecond());
        return newLastValidMessageAndFixedMessages.getFirst();
    }

    public Optional<Message> saveAll(Long deviceId, List<Message> savedMessages) {
        final Pair<Optional<Message>, List<MessageEntity>> lastValidMessageAndFixedMessages
                = this.findLastValidMessageAndFixedMessages(deviceId, savedMessages);
        super.repository.saveAll(lastValidMessageAndFixedMessages.getSecond());
        return lastValidMessageAndFixedMessages.getFirst();
    }

    public Optional<Message> findLastReceivedMessage(Long deviceId) {
        final Optional<MessageEntity> optionalEntity = super.repository
                .findLastReceivedMessage(deviceId);
        return optionalEntity.map(super.mapper::toDto);
    }

    private MessageEntity mapToValidatedEntity(Long deviceId, Message message, Message lastValidMessage) {
        final MessageType messageType = this.messageValidator.validate(message, lastValidMessage);
        final MessageEntity validatedEntity = super.mapper.toEntity(deviceId, message);
        validatedEntity.setType(messageType);
        return validatedEntity;
    }

    private MessageEntity mapToValidatedEntity(Long deviceId, Message message) {
        final MessageType messageType = this.messageValidator.validate(message);
        final MessageEntity entity = super.mapper.toEntity(deviceId, message);
        entity.setType(messageType);
        return entity;
    }

    private Pair<Optional<Message>, List<MessageEntity>> findNewLastMessageAndFixedMessages(
            Long deviceId, Message lastValidMessage, List<Message> fixingMessages) {
        final List<MessageEntity> result = new ArrayList<>();
        boolean newLastMessage = false;
        MessageEntity currentLastMessage = super.mapper.toEntity(deviceId, lastValidMessage);
        MessageEntity currentMessage;
        for (Message fixingMessage : fixingMessages) {
            currentMessage = this.mapToValidatedEntity(deviceId, fixingMessage, lastValidMessage);
            if (currentMessage.getType() == VALID) {
                currentMessage.setGpsOdometer(
                        this.distanceCalculator.calculateDistance(
                                currentLastMessage, currentMessage, this.distanceCalculatorSettings));
                currentLastMessage = currentMessage;
                newLastMessage = true;
            } else if (currentMessage.getType() == CORRECT) {
                currentMessage.setGpsOdometer(
                        this.distanceCalculator.calculateDistance(
                                currentLastMessage, currentMessage, this.distanceCalculatorSettings));
                fixCorrectMessage(currentMessage, currentLastMessage);
            } else {
                currentMessage.setGpsOdometer(0);
            }
            result.add(currentMessage);
        }
        return Pair.of(newLastMessage ? Optional.of(super.mapper.toDto(currentLastMessage)) : empty(), result);
    }

    private Pair<Optional<Message>, List<MessageEntity>> findLastValidMessageAndFixedMessages(
            Long deviceId, List<Message> fixingMessages) {
        final List<MessageEntity> result = new ArrayList<>();
        MessageEntity currentLastMessage = null;
        MessageEntity currentMessage;
        for (Message fixingMessage : fixingMessages) {
            currentMessage = this.mapToValidatedEntity(deviceId, fixingMessage);
            if (currentMessage.getType() == VALID) {
                currentMessage.setGpsOdometer(
                        this.distanceCalculator.calculateDistance(
                                currentLastMessage, currentMessage, this.distanceCalculatorSettings));
                currentLastMessage = currentMessage;
            } else if (currentMessage.getType() == CORRECT) {
                if (currentLastMessage != null) {
                    currentMessage.setGpsOdometer(
                            this.distanceCalculator.calculateDistance(
                                    currentLastMessage, currentMessage, this.distanceCalculatorSettings));
                    fixCorrectMessage(currentMessage, currentLastMessage);
                } else {
                    currentMessage.setGpsOdometer(0);
                    currentMessage.setType(INCORRECT);
                }
            } else {
                currentMessage.setGpsOdometer(0);
            }
            result.add(currentMessage);
        }
        return Pair.of(
                currentLastMessage != null ? Optional.of(super.mapper.toDto(currentLastMessage)) : empty(),
                result);
    }

    private static void fixCorrectMessage(MessageEntity fixedMessage, MessageEntity lastValidMessage) {
        fixedMessage.setLatitude(lastValidMessage.getLatitude());
        fixedMessage.setLongitude(lastValidMessage.getLongitude());
        fixedMessage.setType(VALID);
    }
}
