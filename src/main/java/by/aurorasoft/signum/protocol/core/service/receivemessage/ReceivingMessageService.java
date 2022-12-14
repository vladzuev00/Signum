package by.aurorasoft.signum.protocol.core.service.receivemessage;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import by.aurorasoft.signum.kafka.producer.KafkaInboundMessageProducer;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.core.service.receivemessage.propertycalculator.MessagePropertyCalculator;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.*;
import static java.util.Optional.*;

@Service
@RequiredArgsConstructor
public final class ReceivingMessageService {
    private final ContextManager contextManager;
    private final KafkaInboundMessageProducer kafkaProducer;
    private final MessageTypeIdentifier typeIdentifier;
    private final List<MessagePropertyCalculator> propertyCalculators;

    /**
     * Set 'type' in given messages, fix 'CORRECT' messages to 'VALID'
     * taking GPS coordinates and amount of satellites from last valid message,
     * calculate additional calculated properties for 'VALID' messages.
     * If last valid message isn't exist, then 'CORRECT' messages are saved as 'INCORRECT'.
     * Injects device in each message. Sends result messages to kafka.
     * @param context  - current connection's context.
     * @param messages - messages, which were created by data from tracker:
     *                 without additional properties to be calculated.
     */
    public void receive(ChannelHandlerContext context, List<Message> messages) {
        final Optional<Message> optionalPreviousMessage = this.contextManager.findLastMessage(context);
        final Optional<Message> optionalNewPreviousMessage = optionalPreviousMessage
                .map(previousMessage -> {
                    this.typeMessages(messages, previousMessage);
                    return this.fixAndCalculateProperties(messages, previousMessage);
                })
                .orElseGet(() -> {
                    this.typeMessages(messages);
                    return this.fixAndCalculateProperties(messages);
                });
        optionalNewPreviousMessage.ifPresent(
                newPreviousMessage -> this.contextManager.putLastMessage(context, newPreviousMessage));
        messages.forEach(message -> this.injectDevice(message, context));
        this.kafkaProducer.send(messages);
    }

    private void typeMessages(List<Message> messages) {
        Message lastValid = null;
        MessageType currentType;
        for (final Message message : messages) {
            currentType = lastValid != null
                    ? this.typeIdentifier.identify(message, lastValid)
                    : this.typeIdentifier.identify(message);
            if (currentType == VALID) {
                lastValid = message;
            }
            message.setType(currentType);
        }
    }

    private void typeMessages(List<Message> messages, Message previous) {
        Message lastValid = previous;
        MessageType currentType;
        for (final Message message : messages) {
            currentType = this.typeIdentifier.identify(message, lastValid);
            if (currentType == VALID) {
                lastValid = message;
            }
            message.setType(currentType);
        }
    }

    private Optional<Message> fixAndCalculateProperties(List<Message> messages) {
        Message lastValid = null;
        for (final Message message : messages) {
            if (message.getType() == CORRECT) {
                if (lastValid == null) {
                    message.setType(INCORRECT);
                } else {
                    fixMessageToValid(message, lastValid);
                }
            }
            if (message.getType() == VALID) {
                if (lastValid != null) {
                    this.calculateProperties(message, lastValid);
                } else {
                    this.calculateProperties(message);
                }
                lastValid = message;
            }
        }
        return ofNullable(lastValid);
    }

    private Optional<Message> fixAndCalculateProperties(List<Message> messages, Message previous) {
        Message lastValid = previous;
        for (final Message message : messages) {
            if (message.getType() == CORRECT) {
                fixMessageToValid(message, lastValid);
            }
            if (message.getType() == VALID) {
                this.calculateProperties(message, lastValid);
                lastValid = message;
            }
        }
        return lastValid != previous ? of(lastValid) : empty();
    }

    private static void fixMessageToValid(Message fixed, Message previous) {
        fixed.setCoordinate(previous.getCoordinate());
        fixed.setAmountSatellite(previous.getAmountSatellite());
        fixed.setType(VALID);
    }

    private void calculateProperties(Message message) {
        this.propertyCalculators.forEach(calculator -> calculator.calculate(message));
    }

    private void calculateProperties(Message message, Message previous) {
        this.propertyCalculators.forEach(calculator -> calculator.calculate(message, previous));
    }

    private void injectDevice(Message message, ChannelHandlerContext context) {
        final Device device = this.contextManager.findDevice(context);
        message.setDeviceId(device.getId());
    }
}
