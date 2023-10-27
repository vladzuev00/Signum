package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.RequestPingPackage;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.RequestPingPackage.PACKAGE_PREFIX;

@Component
public final class RequestPingPackageDeserializer extends PackageDeserializer {

    public RequestPingPackageDeserializer() {
        super(PACKAGE_PREFIX);
    }

    @Override
    protected RequestPingPackage deserializeMessage(final String message) {
        return new RequestPingPackage();
    }
}
