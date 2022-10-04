package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.dto.Message;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.PackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.AbstractDataPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.service.MessageService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

import java.util.List;

public abstract class AbstractDataPackageHandler<T extends AbstractDataPackage> extends PackageHandler {
    private final MessageService messageService;

    public AbstractDataPackageHandler(Class<T> packageType, PackageHandler nextHandler, MessageService messageService) {
        super(packageType, nextHandler);
        this.messageService = messageService;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final String doHandle(Package requestPackage, ChannelHandlerContext context) {
        final T dataPackage = (T) requestPackage;
        final List<Message> messages = dataPackage.getMessages();
        final String trackerImei = findTrackerImei(context);
        final int amountSavedMessages = this.messageService.saveAndReturnSavedAmount(messages, trackerImei);
        return this.createResponse(amountSavedMessages);
    }

    protected abstract String createResponse(int amountSavedMessages);

    private static String findTrackerImei(ChannelHandlerContext context) {
        final Channel channel = context.channel();
        final Attribute<String> imeiAttribute = channel.attr(CHANNEL_ATTRIBUTE_KEY_IMEI);
        return imeiAttribute.get();
    }
}
