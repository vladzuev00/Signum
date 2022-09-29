package by.wialontransport.protocol.wialon.handler;

import by.wialontransport.protocol.wialon.model.Package;
import org.springframework.stereotype.Component;

@Component
public final class StarterPackageHandler extends PackageHandler {
    public StarterPackageHandler(LoginPackageHandler nextHandler) {
        super(null, nextHandler);
    }

    @Override
    protected String doHandle(Package requestPackage) {
        throw new UnsupportedOperationException();
    }
}
