package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.DataPackage.PACKAGE_PREFIX;

@Component
public final class DataPackageDecoder extends PackageDecoder {

    public DataPackageDecoder(@Qualifier("blackBoxPackageDecoder") PackageDecoder nextPackageDecoder,
                              @Qualifier("dataPackageDeserializer")
                              PackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, PACKAGE_PREFIX, packageDeserializer);
    }
}
