package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.deserializer.impl.RequestLoginPackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.LoginPackage.PACKAGE_PREFIX;

@Component
public final class LoginPackageDecoder extends PackageDecoder {

    public LoginPackageDecoder(PingPackageDecoder nextPackageDecoder,
                               RequestLoginPackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, PACKAGE_PREFIX, packageDeserializer);
    }
}
