package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.PingPackage;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.PingPackage.PACKAGE_PREFIX;

@Component
public final class PingPackageDeserializer extends PackageDeserializer {

    public PingPackageDeserializer() {
        super(PACKAGE_PREFIX);
    }

    @Override
    protected PingPackage deserializeMessage(final String message) {
        return new PingPackage();
    }
}
