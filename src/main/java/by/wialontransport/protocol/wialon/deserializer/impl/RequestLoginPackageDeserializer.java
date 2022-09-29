package by.wialontransport.protocol.wialon.deserializer.impl;

import by.wialontransport.protocol.wialon.deserializer.PackageDeserializer;
import by.wialontransport.protocol.wialon.model.LoginPackage;
import org.springframework.stereotype.Component;

import static by.wialontransport.protocol.wialon.model.LoginPackage.PACKAGE_DESCRIPTION_POSTFIX;
import static by.wialontransport.protocol.wialon.model.LoginPackage.PACKAGE_DESCRIPTION_PREFIX;

@Component
public final class RequestLoginPackageDeserializer implements PackageDeserializer {
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
        final String message = deserialized
                .replace(PACKAGE_DESCRIPTION_PREFIX, "")
                .replace(PACKAGE_DESCRIPTION_POSTFIX, "");
        return message.split(REGEX_DELIMITER_IMEI_AND_PASSWORD);
    }
}
