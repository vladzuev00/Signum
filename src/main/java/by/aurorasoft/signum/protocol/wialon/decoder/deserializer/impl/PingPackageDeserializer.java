package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.RequestPingPackage;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.RequestPingPackage.PACKAGE_PREFIX;

@Component
public final class PingPackageDeserializer extends PackageDeserializer {

    public PingPackageDeserializer() {
        super(PACKAGE_PREFIX);
    }

    @Override
    protected RequestPingPackage deserializeMessage(final String message) {
        return new RequestPingPackage();
    }
}
