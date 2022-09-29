package by.aurorasoft.signum.protocol.wialon.handler;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import org.springframework.stereotype.Component;

@Component
public final class FinisherPackageHandler extends PackageHandler {
    public FinisherPackageHandler() {
        super(null, null);
    }

    @Override
    protected String doHandle(Package requestPackage) {
        throw new UnsupportedOperationException();
    }
}
