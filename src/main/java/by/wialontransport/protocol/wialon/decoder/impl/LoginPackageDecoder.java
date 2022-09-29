package by.wialontransport.protocol.wialon.decoder.impl;

import by.wialontransport.protocol.wialon.deserializer.impl.RequestLoginPackageDeserializer;
import by.wialontransport.protocol.wialon.decoder.PackageDecoder;
import org.springframework.stereotype.Component;

import static by.wialontransport.protocol.wialon.model.LoginPackage.PACKAGE_DESCRIPTION_PREFIX;

@Component
public final class LoginPackageDecoder extends PackageDecoder {

    public LoginPackageDecoder(PingPackageDecoder nextPackageDecoder,
                               RequestLoginPackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, PACKAGE_DESCRIPTION_PREFIX, packageDeserializer);
    }
}
