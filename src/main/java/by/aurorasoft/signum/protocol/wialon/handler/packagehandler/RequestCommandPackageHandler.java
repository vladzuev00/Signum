package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.service.CommandService;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.wialon.model.RequestCommandPackage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.crud.model.dto.Command.Status.SUCCESS;
import static by.aurorasoft.signum.crud.model.dto.Command.Type.ANSWER;

@Component
public final class RequestCommandPackageHandler extends PackageHandler {
    private static final String RESPONSE_ABOUT_SUCCESS_TO_TRACKER = "#AM#1";

    private final CommandService commandService;
    private final ContextManager contextManager;

    public RequestCommandPackageHandler(FinisherPackageHandler nextHandler, CommandService commandService,
                                        ContextManager contextManager) {
        super(RequestCommandPackage.class, nextHandler);
        this.commandService = commandService;
        this.contextManager = contextManager;
    }

    @Override
    protected void doHandle(Package requestPackage, ChannelHandlerContext context) {
        final RequestCommandPackage requestCommandPackage = (RequestCommandPackage) requestPackage;
        final Device device = this.contextManager.findDevice(context);
        final Command savedCommand = new Command(requestCommandPackage.getMessage(), device.getId());
        this.commandService.save(savedCommand, SUCCESS, ANSWER);
        context.writeAndFlush(RESPONSE_ABOUT_SUCCESS_TO_TRACKER);
    }
}
