package by.aurorasoft.signum.protocol.wialon.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.PingPackage;
import org.springframework.stereotype.Component;

@Component
public final class PingPackageDeserializer implements PackageDeserializer {

    @Override
    public PingPackage deserialize(String deserialized) {
        return new PingPackage();
    }
}
