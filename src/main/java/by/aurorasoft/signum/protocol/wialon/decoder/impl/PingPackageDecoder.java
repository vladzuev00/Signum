package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import by.aurorasoft.signum.protocol.wialon.deserializer.impl.PingPackageDeserializer;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.PingPackage.PACKAGE_DESCRIPTION_PREFIX;

@Component
public final class PingPackageDecoder extends PackageDecoder {

    public PingPackageDecoder(DataPackageDecoder nextPackageDecoder, PingPackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, PACKAGE_DESCRIPTION_PREFIX, packageDeserializer);
    }
}
