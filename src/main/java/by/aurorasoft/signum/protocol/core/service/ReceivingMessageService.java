package by.aurorasoft.signum.protocol.core.service;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.service.message.MessageService;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class ReceivingMessageService {
    private final ContextManager contextManager;
    private final MessageService messageService;

    public void receive(ChannelHandlerContext context, List<Message> messages) {
        final Device device = this.contextManager.findDevice(context);
        final Optional<Message> optionalLastMessage = this.contextManager.findLastMessage(context);
        optionalLastMessage
                .map(lastMessage -> this.messageService.saveAll(device.getId(), lastMessage, messages))
                .orElseGet(() -> this.messageService.saveAll(device.getId(), messages))
                .ifPresent(newLastMessage -> this.contextManager.putLastMessage(context, newLastMessage));
    }
}
