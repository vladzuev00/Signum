package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.exception.NoSuitablePackageHandlerException;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import static io.netty.util.AttributeKey.valueOf;

public abstract class PackageHandler {
    private static final String NAME_CHANNEL_ATTRIBUTE_KEY_IMEI = "imei";
    protected static final AttributeKey<String> CHANNEL_ATTRIBUTE_KEY_IMEI = valueOf(NAME_CHANNEL_ATTRIBUTE_KEY_IMEI);

    private final Class<? extends Package> packageType;
    private final PackageHandler nextHandler;

    public PackageHandler(Class<? extends Package> packageType, PackageHandler nextHandler) {
        this.packageType = packageType;
        this.nextHandler = nextHandler;
    }

    public final String handle(Package requestPackage, ChannelHandlerContext context) {
        if (this.canHandle(requestPackage)) {
            return this.doHandle(requestPackage, context);
        }
        return this.fireNextHandler(requestPackage, context);
    }

    protected abstract String doHandle(Package requestPackage, ChannelHandlerContext context);

    private boolean canHandle(Package requestPackage) {
        return this.packageType != null && this.packageType.isInstance(requestPackage);
    }

    private String fireNextHandler(Package requestPackage, ChannelHandlerContext context) {
        if (this.nextHandler != null) {
            return this.nextHandler.handle(requestPackage, context);
        }
        throw new NoSuitablePackageHandlerException();
    }
}
