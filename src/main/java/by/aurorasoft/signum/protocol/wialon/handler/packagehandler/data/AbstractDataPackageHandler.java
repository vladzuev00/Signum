package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.PackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.AbstractDataPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.crud.service.MessageService;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public abstract class AbstractDataPackageHandler<T extends AbstractDataPackage> extends PackageHandler {
    private final MessageService messageService;
    private final ContextManager contextManager;

    public AbstractDataPackageHandler(Class<T> packageType, PackageHandler nextHandler, MessageService messageService,
                                      ContextManager contextManager) {
        super(packageType, nextHandler);
        this.messageService = messageService;
        this.contextManager = contextManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final void doHandle(Package requestPackage, ChannelHandlerContext context) {
        final T dataPackage = (T) requestPackage;
        final List<Message> messagesToBeSaved = dataPackage.getMessages();
        final Unit unit = this.contextManager.findUnit(context);
        final Device device = unit.getDevice();
        final List<Message> savedMessages = this.messageService.saveAll(device.getId(), messagesToBeSaved);
        context.writeAndFlush(this.createResponse(savedMessages.size()));
    }

    protected abstract String createResponse(int amountSavedMessages);
}
