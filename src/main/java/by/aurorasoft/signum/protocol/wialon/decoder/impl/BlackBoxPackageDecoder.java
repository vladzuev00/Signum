package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.BlackBoxPackageDeserializer;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.BlackBoxPackage.PACKAGE_PREFIX;

@Component
public final class BlackBoxPackageDecoder extends PackageDecoder {

    public BlackBoxPackageDecoder(FinisherPackageDecoder nextPackageDecoder,
                                  BlackBoxPackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, PACKAGE_PREFIX, packageDeserializer);
    }
}
