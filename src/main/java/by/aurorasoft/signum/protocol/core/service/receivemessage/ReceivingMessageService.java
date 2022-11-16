package by.aurorasoft.signum.protocol.core.service.receivemessage;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import by.aurorasoft.signum.crud.service.MessageService;
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
    private final MessageService messageService;
    private final MessageTypeIdentifier typeIdentifier;
    private final List<MessagePropertyCalculator> propertyCalculators;

    public void receive(ChannelHandlerContext context, List<Message> messages) {
        final Optional<Message> optionalPreviousMessage = this.contextManager.findPreviousMessage(context);
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
        final Device device = this.contextManager.findDevice(context);
        this.messageService.saveAll(device.getId(), messages);
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
            if (message.getType() == VALID) {
                lastValid = message;
            } else if (message.getType() == CORRECT) {
                if (lastValid == null) {
                    message.setType(INCORRECT);
                } else {
                    fixMessageToValid(message, lastValid);
                    lastValid = message;
                }
            }
            if (lastValid != null) {
                this.calculateProperties(message, lastValid);
            } else {
                this.calculateProperties(message);
            }
        }
        return ofNullable(lastValid);
    }

    private Optional<Message> fixAndCalculateProperties(List<Message> messages, Message previous) {
        Message lastValid = previous;
        for (final Message message : messages) {
            if (message.getType() == VALID) {
                lastValid = message;
            } else if (message.getType() == CORRECT) {
                fixMessageToValid(message, lastValid);
                lastValid = message;
            }
            this.calculateProperties(message, lastValid);
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
}
