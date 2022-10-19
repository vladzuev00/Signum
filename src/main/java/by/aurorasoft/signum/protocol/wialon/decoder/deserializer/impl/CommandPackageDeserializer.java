package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage;
import by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage.Status;

import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer.removePrefix;
import static by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage.PACKAGE_PREFIX;
import static by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage.Status.findByValue;
import static java.lang.Byte.parseByte;

@Component
public final class CommandPackageDeserializer implements PackageDeserializer {

    @Override
    public ResponseCommandPackage deserialize(String deserialized) {
        final String serializedStatusValue = removePrefix(deserialized, PACKAGE_PREFIX);
        final byte statusValue = parseByte(serializedStatusValue);
        final Status status = findByValue(statusValue);
        return new ResponseCommandPackage(status);
    }
}
