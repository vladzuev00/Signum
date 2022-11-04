package by.aurorasoft.signum.crud.service.message;

import by.aurorasoft.signum.crud.mapper.MessageMapper;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import by.aurorasoft.signum.crud.repository.MessageRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.CORRECT;
import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.VALID;

@Service
@Transactional
public class MessageService
        extends AbsServiceRUD<Long, MessageEntity, Message, MessageMapper, MessageRepository> {
    private final MessageValidator messageValidator;

    public MessageService(MessageMapper mapper, MessageRepository repository, MessageValidator messageValidator) {
        super(mapper, repository);
        this.messageValidator = messageValidator;
    }

    /**
     *
     * @param deviceId
     * @param lastValidMessage
     * @param savedMessages
     * @return new last valid message
     */
    public Message saveAll(Long deviceId, Message lastValidMessage,
                           List<Message> savedMessages) {
        final Pair<Message, List<MessageEntity>> newLastValidMessageAndFixedMessages
                = this.findNewLastValidMessageAndFixedMessages(deviceId, lastValidMessage, savedMessages);
        super.repository.saveAll(newLastValidMessageAndFixedMessages.getSecond());
        return newLastValidMessageAndFixedMessages.getFirst();
    }

    public Optional<Message> findLastReceivedMessage(Long deviceId) {
        final Optional<MessageEntity> optionalEntity = super.repository
                .findLastReceivedMessage(deviceId);
        return optionalEntity.map(super.mapper::toDto);
    }

    private MessageEntity mapToValidatedEntity(Long deviceId, Message message) {
        final MessageType messageType = this.messageValidator.validate(message);
        final MessageEntity validatedEntity = super.mapper.toEntity(deviceId, message);
        validatedEntity.setType(messageType);
        return validatedEntity;
    }

    /**
     * Find new last valid message and fix all 'CORRECT' messages to 'VALID' in list.
     * Gps coordinate of 'CORRECT' messages are taken from current last valid message.
     * @param fixingMessages - messages to correct.
     * @param deviceId - device's id.
     * @param lastValidMessage - current last valid message
     * @return pair of new last valid message and corrected messages.
     */
    private Pair<Message, List<MessageEntity>> findNewLastValidMessageAndFixedMessages(
            Long deviceId, Message lastValidMessage, List<Message> fixingMessages) {
        final List<MessageEntity> result = new ArrayList<>();
        MessageEntity currentLastMessage = super.mapper.toEntity(deviceId, lastValidMessage);
        MessageEntity currentMessage;
        for (Message fixingMessage : fixingMessages) {
            currentMessage = this.mapToValidatedEntity(deviceId, fixingMessage);
            if (currentMessage.getType() == VALID) {
                currentLastMessage = currentMessage;
            } else if (currentMessage.getType() == CORRECT) {
                fixCorrectMessage(currentMessage, currentLastMessage);
            }
            result.add(currentMessage);
        }
        return Pair.of(super.mapper.toDto(currentLastMessage), result);
    }

    private static void fixCorrectMessage(MessageEntity fixedMessage, MessageEntity lastValidMessage) {
        fixedMessage.setLatitude(lastValidMessage.getLatitude());
        fixedMessage.setLongitude(lastValidMessage.getLongitude());
        fixedMessage.setType(VALID);
    }
}
