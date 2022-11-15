package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.core.service.ReceivingMessageService;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.PackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.AbstractDataPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public abstract class AbstractDataPackageHandler<T extends AbstractDataPackage> extends PackageHandler {
    private final ReceivingMessageService receivingMessageService;

    public AbstractDataPackageHandler(Class<T> packageType, PackageHandler nextHandler,
                                      ReceivingMessageService receivingMessageService) {
        super(packageType, nextHandler);
        this.receivingMessageService = receivingMessageService;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final void doHandle(Package requestPackage, ChannelHandlerContext context) {
        final T dataPackage = (T) requestPackage;
        final List<Message> messages = dataPackage.getMessages();
        this.receivingMessageService.receive(context, messages);
        context.writeAndFlush(this.createResponse(messages.size()));
    }

    protected abstract String createResponse(int amountSavedMessages);
}
