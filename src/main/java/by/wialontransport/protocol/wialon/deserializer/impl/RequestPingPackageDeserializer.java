package by.wialontransport.protocol.wialon.deserializer.impl;

import by.wialontransport.protocol.wialon.deserializer.PackageDeserializer;
import by.wialontransport.protocol.wialon.model.PingPackage;
import org.springframework.stereotype.Component;

@Component
public final class RequestPingPackageDeserializer implements PackageDeserializer {

    @Override
    public PingPackage deserialize(String deserialized) {
        return new PingPackage();
    }
}
