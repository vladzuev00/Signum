package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.exception.NoSuitablePackageHandlerException;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;

import java.util.Optional;

public abstract class PackageHandler {
    private final Class<? extends Package> packageType;
    private final PackageHandler nextHandler;

    public PackageHandler(Class<? extends Package> packageType, PackageHandler nextHandler) {
        this.packageType = packageType;
        this.nextHandler = nextHandler;
    }

    public final Optional<String> handle(Package inboundPackage, ChannelHandlerContext context) {
        if (this.canHandle(inboundPackage)) {
            return this.doHandle(inboundPackage, context);
        }
        return this.fireNextHandler(inboundPackage, context);
    }

    protected abstract Optional<String> doHandle(Package requestPackage, ChannelHandlerContext context);

    private boolean canHandle(Package requestPackage) {
        return this.packageType != null && this.packageType.isInstance(requestPackage);
    }

    private Optional<String> fireNextHandler(Package requestPackage, ChannelHandlerContext context) {
        if (this.nextHandler != null) {
            return this.nextHandler.handle(requestPackage, context);
        }
        throw new NoSuitablePackageHandlerException();
    }
}
