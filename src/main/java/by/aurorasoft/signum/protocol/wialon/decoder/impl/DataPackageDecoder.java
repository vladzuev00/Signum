package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data.RequestDataPackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.RequestDataPackage;
import org.springframework.stereotype.Component;

@Component
public final class DataPackageDecoder extends PackageDecoder {

    public DataPackageDecoder(BlackBoxPackageDecoder nextPackageDecoder,
                              RequestDataPackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, RequestDataPackage.PACKAGE_PREFIX, packageDeserializer);
    }
}
