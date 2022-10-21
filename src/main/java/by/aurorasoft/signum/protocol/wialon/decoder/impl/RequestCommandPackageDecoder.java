package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.RequestCommandPackageDeserializer;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.RequestCommandPackage.PACKAGE_PREFIX;

@Component
public final class RequestCommandPackageDecoder extends PackageDecoder {

    public RequestCommandPackageDecoder(FinisherPackageDecoder nextDecoder,
                                        RequestCommandPackageDeserializer packageDeserializer) {
        super(nextDecoder, PACKAGE_PREFIX, packageDeserializer);
    }
}
