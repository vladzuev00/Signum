package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.wialon.model.PingPackage;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.PingPackage.PACKAGE_DESCRIPTION_PREFIX;

@Component
public final class PingPackageDeserializer extends PackageDeserializer {

    public PingPackageDeserializer() {
        super(PACKAGE_DESCRIPTION_PREFIX);
    }

    @Override
    protected Package deserializeMessage(final String message) {
        return new PingPackage();
    }
}
