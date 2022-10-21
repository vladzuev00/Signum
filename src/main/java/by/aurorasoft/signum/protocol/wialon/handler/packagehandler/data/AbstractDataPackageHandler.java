package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

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
    private final ContextManager contextWorker;

    public AbstractDataPackageHandler(Class<T> packageType, PackageHandler nextHandler, MessageService messageService,
                                      ContextManager contextWorker) {
        super(packageType, nextHandler);
        this.messageService = messageService;
        this.contextWorker = contextWorker;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final void doHandle(Package requestPackage, ChannelHandlerContext context) {
        final T dataPackage = (T) requestPackage;
        final List<Message> messagesToBeSaved = dataPackage.getMessages();
        final Unit unit = this.contextWorker.findUnit(context);
        final List<Message> savedMessages = this.messageService.saveAll(unit.getId(), messagesToBeSaved);
        context.writeAndFlush(this.createResponse(savedMessages.size()));
    }

    protected abstract String createResponse(int amountSavedMessages);
}
