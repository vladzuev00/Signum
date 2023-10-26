package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.LoginPackage.PACKAGE_PREFIX;

@Component
public final class LoginPackageDeserializer extends PackageDeserializer {
    private static final String REGEX_COMPONENT_DELIMITER = ";";
    private static final int INDEX_COMPONENT_IMEI = 0;
    private static final int INDEX_COMPONENT_PASSWORD = 1;

    public LoginPackageDeserializer() {
        super(PACKAGE_PREFIX);
    }

    @Override
    protected Package deserializeMessage(final String message) {
        final String[] components = message.split(REGEX_COMPONENT_DELIMITER);
        final String imei = components[INDEX_COMPONENT_IMEI];
        final String password = components[INDEX_COMPONENT_PASSWORD];
        return new LoginPackage(imei, password);
    }
}
