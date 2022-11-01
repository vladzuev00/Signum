package by.aurorasoft.signum.protocol.core.service;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.service.CommandService;
import by.aurorasoft.signum.protocol.core.connectionmanager.ConnectionManager;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

import static by.aurorasoft.signum.crud.model.dto.Command.Status.NEW;
import static by.aurorasoft.signum.crud.model.dto.Command.Status.SENT;
import static by.aurorasoft.signum.crud.model.dto.Command.Type.COMMAND;
import static java.lang.String.format;

//TODO: correct dependencies
@Service
public class CommandSenderService {
    private static final Command.Status[] STATUSES_OF_COMMANDS_TO_BE_RESENT = { NEW, SENT };

    private final CommandService commandService;
    private final CommandSerializer commandSerializer;
    private final ConnectionManager connectionManager;
    private final ContextManager contextManager;

    public CommandSenderService(CommandService commandService, CommandSerializer commandSerializer,
                                ConnectionManager connectionManager, ContextManager contextManager) {
        this.commandService = commandService;
        this.commandSerializer = commandSerializer;
        this.connectionManager = connectionManager;
        this.contextManager = contextManager;
    }

    public void send(Collection<Command> commands) {
        commands.forEach(this::send);
    }

    public void send(Command command) {
        final Command savedCommand = this.commandService.save(command, NEW, COMMAND);
        this.sendIfTrackerIsConnected(savedCommand);
    }

    public void resendCommands(Device device) {
        final List<Command> commandsToBeResent = this.commandService
                .findCommandsByDeviceAndStatuses(device, STATUSES_OF_COMMANDS_TO_BE_RESENT);
        commandsToBeResent.forEach(this::send);
    }

    @SuppressWarnings("all")
    public void onSentCommandWasHandled(ChannelHandlerContext context) {
        synchronized (context) {
            if (this.contextManager.isExistCommandToBeSent(context)) {
                final Command commandToBeSent = this.contextManager.findCommandToBeSent(context);
                this.sendIfTrackerIsConnected(commandToBeSent);
            }
        }
    }

    private void sendIfTrackerIsConnected(Command command) {
        final Optional<ChannelHandlerContext> optionalContext = this.connectionManager
                .find(command.getDeviceId());
        optionalContext.ifPresent(context -> this.sendToConnectedTracker(command, context));
    }

    @SuppressWarnings("all")
    private void sendToConnectedTracker(Command command, ChannelHandlerContext context) {
        synchronized (context) {
            if (this.contextManager.isExistCommandWaitingResponse(context)) {
                this.addCommandToSendLater(command, context);
            } else {
                this.sendCommandNow(command, context);
            }
        }
    }

    private void addCommandToSendLater(Command command, ChannelHandlerContext context) {
        this.contextManager.putCommandToBeSent(context, command);
    }

    private void sendCommandNow(Command command, ChannelHandlerContext context) {
        final String serializedCommand = this.commandSerializer.serialize(command);
        this.contextManager.putCommandWaitingResponse(context, command);
        context.writeAndFlush(serializedCommand)
                .addListener((ChannelFutureListener)
                        future -> this.commandService.updateStatus(command, SENT));
    }

    @Component
    static final class CommandSerializer {
        private static final String TEMPLATE_SENT_COMMAND = "#M#%s";

        public CommandSerializer() {

        }

        public String serialize(Command command) {
            return format(TEMPLATE_SENT_COMMAND, command.getText());
        }
    }
}
