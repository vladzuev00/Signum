package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.ResponseCommandPackageDeserializer;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage.PACKAGE_PREFIX;

@Component
public final class ResponseCommandPackageDecoder extends PackageDecoder {

    public ResponseCommandPackageDecoder(RequestCommandPackageDecoder nextDecoder,
                                         ResponseCommandPackageDeserializer packageDeserializer) {
        super(nextDecoder, PACKAGE_PREFIX, packageDeserializer);
    }
}
