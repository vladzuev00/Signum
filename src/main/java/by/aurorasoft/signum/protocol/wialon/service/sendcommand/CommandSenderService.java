package by.aurorasoft.signum.protocol.wialon.service.sendcommand;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.service.CommandService;
import by.aurorasoft.signum.protocol.core.connectionmanager.ConnectionManager;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.NEW;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.SENT;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Type.COMMAND;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public final class CommandSenderService {
    private static final String TEMPLATE_SENT_COMMAND = "#M#%s";

    private final ConnectionManager connectionManager;
    private final CommandService commandService;
    private final ContextManager contextManager;

    /**
     * Operation is blocked while context already has command waiting response
     */
    public void send(Command command) {
        final Command savedCommand = this.commandService.save(command, NEW, COMMAND);
        final String sentCommand = format(TEMPLATE_SENT_COMMAND, savedCommand.getText());
        final Optional<ChannelHandlerContext> optionalContext = this.connectionManager
                .findContext(savedCommand.getTracker());
        optionalContext.ifPresent(
                context -> {
                    this.contextManager.putCommandWaitingResponse(context, savedCommand);
                    context.writeAndFlush(sentCommand)
                            .addListener(
                                    (ChannelFutureListener)
                                            future -> this.commandService.updateByGivenStatus(savedCommand, SENT));
                }
        );
    }

    public void send(List<Command> commands) {
        commands.forEach(this::send);
    }
}
