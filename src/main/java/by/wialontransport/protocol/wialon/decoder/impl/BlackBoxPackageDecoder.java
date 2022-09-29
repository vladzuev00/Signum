package by.wialontransport.protocol.wialon.decoder.impl;

import by.wialontransport.protocol.wialon.deserializer.PackageDeserializer;
import by.wialontransport.protocol.wialon.decoder.PackageDecoder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static by.wialontransport.protocol.wialon.model.BlackBoxPackage.PACKAGE_DESCRIPTION_PREFIX;

@Component
public final class BlackBoxPackageDecoder extends PackageDecoder {

    public BlackBoxPackageDecoder(@Qualifier("finisherPackageDecoder") PackageDecoder nextPackageDecoder,
                                  @Qualifier("requestBlackBoxPackageDeserializer")
                                  PackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, PACKAGE_DESCRIPTION_PREFIX, packageDeserializer);
    }
}
