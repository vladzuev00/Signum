package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.PackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.AbstractDataPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.crud.service.message.MessageService;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public abstract class AbstractDataPackageHandler<T extends AbstractDataPackage> extends PackageHandler {
    private final MessageService messageService;
    private final ContextManager contextManager;

    public AbstractDataPackageHandler(Class<T> packageType, PackageHandler nextHandler,
                                      MessageService messageService, ContextManager contextManager) {
        super(packageType, nextHandler);
        this.messageService = messageService;
        this.contextManager = contextManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final void doHandle(Package requestPackage, ChannelHandlerContext context) {
        final T dataPackage = (T) requestPackage;
        final List<Message> messages = dataPackage.getMessages();
        final Device device = this.contextManager.findDevice(context);
        final Message lastValidMessage = this.contextManager.findLastValidReceivedMessage(context);
        final Message newLastValidMessage = this.messageService.saveAll(
                device.getId(), lastValidMessage, messages);
        this.contextManager.putLastMessage(context, newLastValidMessage);
        context.writeAndFlush(this.createResponse(messages.size()));
    }

    protected abstract String createResponse(int amountSavedMessages);
}
