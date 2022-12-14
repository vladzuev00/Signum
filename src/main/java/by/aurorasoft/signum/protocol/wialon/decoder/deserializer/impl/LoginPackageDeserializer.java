package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer.removePrefix;
import static by.aurorasoft.signum.protocol.wialon.model.LoginPackage.PACKAGE_PREFIX;

@Component
public final class LoginPackageDeserializer implements PackageDeserializer {
    private static final String REGEX_DELIMITER_IMEI_AND_PASSWORD = ";";
    private static final int INDEX_IMEI = 0;
    private static final int INDEX_PASSWORD = 1;

    @Override
    public LoginPackage deserialize(String deserialized) {
        final String[] messageComponents = findMessageComponents(deserialized);
        final String imei = messageComponents[INDEX_IMEI];
        final String password = messageComponents[INDEX_PASSWORD];
        return new LoginPackage(imei, password);
    }

    private static String[] findMessageComponents(String deserialized) {
        final String message = removePrefix(deserialized, PACKAGE_PREFIX);
        return message.split(REGEX_DELIMITER_IMEI_AND_PASSWORD);
    }
}
