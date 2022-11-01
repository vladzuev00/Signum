package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.service.CommandService;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class ResponseCommandPackageHandler extends PackageHandler {
    private final CommandService commandService;
    private final ContextManager contextManager;

    public ResponseCommandPackageHandler(RequestCommandPackageHandler nextHandler, CommandService commandService,
                                         ContextManager contextManager) {
        super(ResponseCommandPackage.class, nextHandler);
        this.commandService = commandService;
        this.contextManager = contextManager;
    }

    @Override
    protected void doHandle(Package inboundPackage, ChannelHandlerContext context) {
        final ResponseCommandPackage responseCommandPackage = (ResponseCommandPackage) inboundPackage;
        final Optional<Command> optionalCommandWaitingResponse = this.contextManager
                .findCommandWaitingResponse(context);
        optionalCommandWaitingResponse.ifPresent(commandWaitingResponse -> {
            this.contextManager.onGetCommandResponse(context);
            final Command.Status statusAnsweredCommand =
                    responseCommandPackage.getStatus() == ResponseCommandPackage.Status.SUCCESS
                            ? Command.Status.SUCCESS
                            : Command.Status.ERROR;
            this.commandService.updateStatus(commandWaitingResponse, statusAnsweredCommand);
        });
    }
}
