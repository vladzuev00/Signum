package by.aurorasoft.signum.protocol.wialon.decoder.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.LoginPackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.decoder.PackageDecoder;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.RequestLoginPackage.PACKAGE_PREFIX;

@Component
public final class LoginPackageDecoder extends PackageDecoder {

    public LoginPackageDecoder(PingPackageDecoder nextPackageDecoder,
                               LoginPackageDeserializer packageDeserializer) {
        super(nextPackageDecoder, PACKAGE_PREFIX, packageDeserializer);
    }
}
