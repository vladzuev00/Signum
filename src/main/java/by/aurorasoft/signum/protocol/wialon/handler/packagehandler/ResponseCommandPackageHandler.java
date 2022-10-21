package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.service.CommandService;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

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
        final Command commandWaitingResponse = this.contextManager.findCommandWaitingResponse(context);
        final CommandEntity.Status statusAnsweredCommand =
                responseCommandPackage.getStatus() == ResponseCommandPackage.Status.SUCCESS
                        ? CommandEntity.Status.SUCCESS
                        : CommandEntity.Status.ERROR;
        this.commandService.updateByGivenStatus(commandWaitingResponse, statusAnsweredCommand);
    }
}
