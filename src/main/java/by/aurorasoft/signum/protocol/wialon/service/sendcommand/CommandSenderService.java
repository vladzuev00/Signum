package by.aurorasoft.signum.protocol.wialon.service.sendcommand;

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
import java.util.concurrent.ConcurrentHashMap;

import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.NEW;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.SENT;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Type.COMMAND;
import static java.lang.String.format;
import static org.springframework.util.CollectionUtils.isEmpty;

//TODO: correct dependencies
@Service
public class CommandSenderService {
    private final CommandService commandService;
    private final CommandSerializer commandSerializer;
    private final ConnectionManager connectionManager;
    private final ContextManager contextManager;
    private final Map<Device, Queue<Command>> commandsToBeSentLater;

    public CommandSenderService(CommandService commandService, CommandSerializer commandSerializer,
                                ConnectionManager connectionManager, ContextManager contextManager) {
        this.commandService = commandService;
        this.commandSerializer = commandSerializer;
        this.connectionManager = connectionManager;
        this.contextManager = contextManager;
        this.commandsToBeSentLater = new ConcurrentHashMap<>();
    }

    public void send(Collection<Command> commands) {
        commands.forEach(this::send);
    }

    public void send(Command command) {
        final Command savedCommand = this.commandService.save(command, NEW, COMMAND);
        this.sendIfTrackerIsConnected(savedCommand);
    }

    @SuppressWarnings("ConstantConditions")
    public void onSentCommandWasHandled(Device tracker) {
        final Queue<Command> commandsToBeSent = this.commandsToBeSentLater.get(tracker);
        if (!isEmpty(commandsToBeSent)) {
            final Command commandToBeSent = commandsToBeSent.poll();
            this.sendIfTrackerIsConnected(commandToBeSent);
        }
    }

    private void sendIfTrackerIsConnected(Command command) {
        final Optional<ChannelHandlerContext> optionalContext = this.connectionManager
                .findContext(command.getDevice());
        optionalContext.ifPresent(context -> this.sendToConnectedTracker(command, context));
    }

    @SuppressWarnings("all")
    private void sendToConnectedTracker(Command command, ChannelHandlerContext context) {
        synchronized (context) {
            if (this.contextManager.isExistCommandWaitingResponse(context)) {
                this.addCommandToSendLater(command);
            } else {
                this.sendCommandNow(command, context);
            }
        }
    }

    private void addCommandToSendLater(Command command) {
        this.commandsToBeSentLater.merge(
                command.getDevice(),
                new LinkedList<>(List.of(command)),
                CommandSenderService::append);
    }

    private static Queue<Command> append(Queue<Command> source, Queue<Command> appended) {
        source.addAll(appended);
        return source;
    }

    private void sendCommandNow(Command command, ChannelHandlerContext context) {
        final String serializedCommand = this.commandSerializer.serialize(command);
        this.contextManager.putCommandWaitingResponse(context, command);
        context.writeAndFlush(serializedCommand)
                .addListener((ChannelFutureListener)
                        future -> this.commandService.updateByGivenStatus(command, SENT));
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
