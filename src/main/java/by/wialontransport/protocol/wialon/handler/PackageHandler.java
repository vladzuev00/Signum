package by.wialontransport.protocol.wialon.handler;

import by.wialontransport.protocol.wialon.handler.exception.NoSuitablePackageAnswererException;
import by.wialontransport.protocol.wialon.model.Package;

public abstract class PackageHandler {
    private final Class<? extends Package> packageType;
    private final PackageHandler nextHandler;

    public PackageHandler(Class<? extends Package> packageType, PackageHandler nextHandler) {
        this.packageType = packageType;
        this.nextHandler = nextHandler;
    }

    public final String handle(Package requestPackage) {
        if (this.canHandle(requestPackage)) {
            return this.doHandle(requestPackage);
        }
        return this.fireNextHandler(requestPackage);
    }

    protected abstract String doHandle(Package requestPackage);

    private boolean canHandle(Package requestPackage) {
        return this.packageType != null && this.packageType.isInstance(requestPackage);
    }

    private String fireNextHandler(Package requestPackage) {
        if (this.nextHandler != null) {
            return this.nextHandler.handle(requestPackage);
        }
        throw new NoSuitablePackageAnswererException();
    }
}
