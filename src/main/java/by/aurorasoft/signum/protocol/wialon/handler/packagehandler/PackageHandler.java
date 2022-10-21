package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.exception.NoSuitablePackageHandlerException;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;

public abstract class PackageHandler {
    private final Class<? extends Package> packageType;
    private final PackageHandler nextHandler;

    public PackageHandler(Class<? extends Package> packageType, PackageHandler nextHandler) {
        this.packageType = packageType;
        this.nextHandler = nextHandler;
    }

    public final void handle(Package inboundPackage, ChannelHandlerContext context) {
        if (this.canHandle(inboundPackage)) {
            this.doHandle(inboundPackage, context);
            return;
        }
        this.fireNextHandler(inboundPackage, context);
    }

    protected abstract void doHandle(Package requestPackage, ChannelHandlerContext context);

    private boolean canHandle(Package requestPackage) {
        return this.packageType != null && this.packageType.isInstance(requestPackage);
    }

    private void fireNextHandler(Package requestPackage, ChannelHandlerContext context) {
        if (this.nextHandler == null) {
            throw new NoSuitablePackageHandlerException();
        }
        this.nextHandler.handle(requestPackage, context);
    }
}
