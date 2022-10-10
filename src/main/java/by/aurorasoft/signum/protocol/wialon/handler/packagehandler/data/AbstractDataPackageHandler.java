package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.dto.ChannelUnitDto;
import by.aurorasoft.signum.dto.MessageDto;
import by.aurorasoft.signum.protocol.wialon.handler.contextworker.ContextWorker;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.PackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.AbstractDataPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.service.MessageService;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public abstract class AbstractDataPackageHandler<T extends AbstractDataPackage> extends PackageHandler {
    private final MessageService messageService;
    private final ContextWorker contextWorker;

    public AbstractDataPackageHandler(Class<T> packageType, PackageHandler nextHandler, MessageService messageService,
                                      ContextWorker contextWorker) {
        super(packageType, nextHandler);
        this.messageService = messageService;
        this.contextWorker = contextWorker;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final String doHandle(Package requestPackage, ChannelHandlerContext context) {
        final T dataPackage = (T) requestPackage;
        final List<MessageDto> messagesToBeSaved = dataPackage.getMessages();
        final ChannelUnitDto unit = this.contextWorker.findUnit(context);
        final List<MessageDto> savedMessages = this.messageService.saveAll(unit.getId(), messagesToBeSaved);
        return this.createResponse(savedMessages.size());
    }

    protected abstract String createResponse(int amountSavedMessages);
}
