package by.aurorasoft.signum.protocol.wialon.service.sendcommand;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Tracker;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.service.CommandService;
import by.aurorasoft.signum.protocol.wialon.connectionmanager.ConnectionManager;
import by.aurorasoft.signum.protocol.wialon.contextmanager.ContextManager;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.NEW;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.SENT;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public final class SendCommandService {
    private static final String TEMPLATE_SENT_COMMAND = "#M#%s";

    private final ConnectionManager connectionManager;
    private final CommandService commandService;
    private final ContextManager contextManager;

    public void sendCommand(Command command) {
        final Command savedCommand = this.commandService.saveWithGivenStatus(command, NEW);
        final String sentCommand = format(TEMPLATE_SENT_COMMAND, command.getText());
        final ChannelHandlerContext context = this.connectionManager.findContext(command.getTracker());
        context.writeAndFlush(sentCommand)
                .addListener((ChannelFutureListener)
                        future -> {
                            this.contextManager.putCommandWaitingResponse(context, savedCommand);
                            this.commandService.updateByGivenStatus(savedCommand, SENT);
                        });
    }
}
