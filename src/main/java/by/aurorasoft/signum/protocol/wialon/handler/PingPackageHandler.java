package by.aurorasoft.signum.protocol.wialon.handler;

import by.aurorasoft.signum.protocol.wialon.handler.data.DataPackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.wialon.model.PingPackage;
import org.springframework.stereotype.Component;

@Component
public final class PingPackageHandler extends PackageHandler {
    private static final String RESPONSE_PACKAGE = "#AP#\r\n";

    public PingPackageHandler(DataPackageHandler nextHandler) {
        super(PingPackage.class, nextHandler);
    }

    @Override
    protected String doHandle(Package requestPackage) {
        return RESPONSE_PACKAGE;
    }
}
