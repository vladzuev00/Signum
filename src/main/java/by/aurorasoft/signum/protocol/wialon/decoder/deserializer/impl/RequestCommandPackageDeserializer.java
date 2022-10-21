package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.RequestCommandPackage;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer.removePrefix;
import static by.aurorasoft.signum.protocol.wialon.model.RequestCommandPackage.PACKAGE_PREFIX;

@Component
public final class RequestCommandPackageDeserializer implements PackageDeserializer {

    @Override
    public RequestCommandPackage deserialize(String deserialized) {
        final String message = removePrefix(deserialized, PACKAGE_PREFIX);
        return new RequestCommandPackage(message);
    }
}
