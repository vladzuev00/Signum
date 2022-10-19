package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.service.CommandService;
import by.aurorasoft.signum.protocol.wialon.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.ERROR_ANSWERED;
import static by.aurorasoft.signum.crud.model.entity.CommandEntity.Status.SUCCESS_ANSWERED;
import static by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage.Status.SUCCESS;
import static java.util.Optional.empty;

@Component
public final class CommandPackageHandler extends PackageHandler {
    private final CommandService commandService;
    private final ContextManager contextManager;

    public CommandPackageHandler(FinisherPackageHandler nextHandler, CommandService commandService,
                                 ContextManager contextManager) {
        super(ResponseCommandPackage.class, nextHandler);
        this.commandService = commandService;
        this.contextManager = contextManager;
    }

    @Override
    protected Optional<String> doHandle(Package inboundPackage, ChannelHandlerContext context) {
        final ResponseCommandPackage responseCommandPackage = (ResponseCommandPackage) inboundPackage;
        final Command commandWaitingResponse = this.contextManager.findCommandWaitingResponse(context);
        final CommandEntity.Status statusAnsweredCommand =
                responseCommandPackage.getStatus() == SUCCESS ? SUCCESS_ANSWERED : ERROR_ANSWERED;
        this.commandService.updateByGivenStatus(commandWaitingResponse, statusAnsweredCommand);
        return empty();
    }
}
