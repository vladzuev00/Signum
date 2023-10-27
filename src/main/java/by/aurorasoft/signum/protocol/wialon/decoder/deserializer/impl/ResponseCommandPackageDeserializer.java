package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage;
import by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage.Status;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage.PACKAGE_PREFIX;
import static by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage.Status.findByValue;
import static java.lang.Byte.parseByte;

@Component
public final class ResponseCommandPackageDeserializer extends PackageDeserializer {

    public ResponseCommandPackageDeserializer() {
        super(PACKAGE_PREFIX);
    }

    @Override
    protected ResponseCommandPackage deserializeMessage(final String message) {
        final Status status = findCommandStatus(message);
        return new ResponseCommandPackage(status);
    }

    private static Status findCommandStatus(final String message) {
        final byte statusValue = parseByte(message);
        return findByValue(statusValue);
    }
}
