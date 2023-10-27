package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.RequestCommandPackage.PACKAGE_PREFIX;

@Component
public final class RequestCommandPackageDecoder extends PackageDecoder {

    public RequestCommandPackageDecoder(FinisherPackageDecoder nextDecoder) {
        super(nextDecoder, PACKAGE_PREFIX, null);
    }
}
