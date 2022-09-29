package by.wialontransport.protocol.wialon.decoder.impl;

import by.wialontransport.protocol.wialon.deserializer.PackageDeserializer;
import by.wialontransport.protocol.wialon.decoder.PackageDecoder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static by.wialontransport.protocol.wialon.model.DataPackage.PACKAGE_DESCRIPTION_PREFIX;

@Component
public final class DataPackageDecoder extends PackageDecoder {

    public DataPackageDecoder(@Qualifier("blackBoxPackageDecoder") PackageDecoder nextPackageDecoder,
                              @Qualifier("requestDataPackageDeserializer")
                              PackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, PACKAGE_DESCRIPTION_PREFIX, packageDeserializer);
    }
}
