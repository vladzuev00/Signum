package by.aurorasoft.signum.protocol.wialon.deserializer.impl;

import by.aurorasoft.signum.entity.Message;
import by.aurorasoft.signum.protocol.wialon.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.deserializer.impl.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.model.DataPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.DataPackage.PACKAGE_PREFIX;
import static by.aurorasoft.signum.protocol.wialon.model.Package.PACKAGE_POSTFIX;
import static java.util.Collections.singletonList;

@RequiredArgsConstructor
@Component
public final class RequestDataPackageDeserializer implements PackageDeserializer {
    private final MessageParser messageDeserializer;

    @Override
    public DataPackage deserialize(String deserialized) {
        final String serializedMessage = deserialized
                .replace(PACKAGE_PREFIX, "")
                .replace(PACKAGE_POSTFIX, "");
        final Message message = this.messageDeserializer.parse(serializedMessage);
        return new DataPackage(singletonList(message));
    }
}
