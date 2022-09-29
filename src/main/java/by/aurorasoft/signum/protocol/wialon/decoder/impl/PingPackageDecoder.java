package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.PingPackage.PACKAGE_DESCRIPTION_PREFIX;

@Component
public final class PingPackageDecoder extends PackageDecoder {

    public PingPackageDecoder(@Qualifier("dataPackageDecoder") PackageDecoder nextPackageDecoder,
                              @Qualifier("requestPingPackageDeserializer")
                              PackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, PACKAGE_DESCRIPTION_PREFIX, packageDeserializer);
    }
}
