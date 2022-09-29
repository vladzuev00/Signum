package by.wialontransport.protocol.wialon.decoder.impl;

import by.wialontransport.protocol.wialon.deserializer.PackageDeserializer;
import by.wialontransport.protocol.wialon.decoder.PackageDecoder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static by.wialontransport.protocol.wialon.model.PingPackage.PACKAGE_DESCRIPTION_PREFIX;

@Component
public final class PingPackageDecoder extends PackageDecoder {

    public PingPackageDecoder(@Qualifier("dataPackageDecoder") PackageDecoder nextPackageDecoder,
                              @Qualifier("requestPingPackageDeserializer")
                              PackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, PACKAGE_DESCRIPTION_PREFIX, packageDeserializer);
    }
}
