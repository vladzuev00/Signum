package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.entity.Message;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.PackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.AbstractDataPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.service.MessageService;

import java.util.List;

public abstract class AbstractDataPackageHandler<T extends AbstractDataPackage> extends PackageHandler {
    private final MessageService messageService;

    public AbstractDataPackageHandler(Class<T> packageType, PackageHandler nextHandler, MessageService messageService) {
        super(packageType, nextHandler);
        this.messageService = messageService;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final String doHandle(Package requestPackage) {
        final T dataPackage = (T) requestPackage;
        final List<Message> messages = dataPackage.getMessages();
        final int amountSavedMessages = this.messageService.saveAndReturnSavedAmount(messages);
        return this.createResponse(amountSavedMessages);
    }

    protected abstract String createResponse(int amountSavedMessages);
}
