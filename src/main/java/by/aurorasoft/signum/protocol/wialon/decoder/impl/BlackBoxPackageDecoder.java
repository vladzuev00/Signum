package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.BlackBoxPackage.PACKAGE_DESCRIPTION_PREFIX;

@Component
public final class BlackBoxPackageDecoder extends PackageDecoder {

    public BlackBoxPackageDecoder(@Qualifier("finisherPackageDecoder") PackageDecoder nextPackageDecoder,
                                  @Qualifier("requestBlackBoxPackageDeserializer")
                                  PackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, PACKAGE_DESCRIPTION_PREFIX, packageDeserializer);
    }
}
