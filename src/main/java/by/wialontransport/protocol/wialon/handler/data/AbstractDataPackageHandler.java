package by.wialontransport.protocol.wialon.handler.data;

import by.wialontransport.entity.Message;
import by.wialontransport.protocol.wialon.handler.PackageHandler;
import by.wialontransport.protocol.wialon.model.AbstractDataPackage;
import by.wialontransport.protocol.wialon.model.Package;
import by.wialontransport.service.MessageService;

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
