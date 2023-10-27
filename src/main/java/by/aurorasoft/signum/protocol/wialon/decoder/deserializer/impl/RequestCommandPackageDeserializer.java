package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.RequestCommandPackage;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.RequestCommandPackage.PACKAGE_PREFIX;

@Component
public final class RequestCommandPackageDeserializer extends PackageDeserializer {

    public RequestCommandPackageDeserializer() {
        super(PACKAGE_PREFIX);
    }

    @Override
    protected RequestCommandPackage deserializeMessage(final String message) {
        return new RequestCommandPackage(message);
    }
}
