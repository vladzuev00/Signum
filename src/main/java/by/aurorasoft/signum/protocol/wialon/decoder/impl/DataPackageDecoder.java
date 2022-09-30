package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import by.aurorasoft.signum.protocol.wialon.deserializer.impl.DataPackageDeserializer;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.DataPackage.PACKAGE_PREFIX;

@Component
public final class DataPackageDecoder extends PackageDecoder {

    public DataPackageDecoder(BlackBoxPackageDecoder nextPackageDecoder,
                              DataPackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, PACKAGE_PREFIX, packageDeserializer);
    }
}
