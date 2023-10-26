package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data.DataPackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.DataPackage;
import org.springframework.stereotype.Component;

@Component
public final class DataPackageDecoder extends PackageDecoder {

    public DataPackageDecoder(BlackBoxPackageDecoder nextPackageDecoder,
                              DataPackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, DataPackage.PACKAGE_PREFIX, packageDeserializer);
    }
}
