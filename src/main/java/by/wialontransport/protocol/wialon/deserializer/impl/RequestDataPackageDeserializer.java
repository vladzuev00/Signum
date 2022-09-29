package by.wialontransport.protocol.wialon.deserializer.impl;

import by.wialontransport.entity.Message;
import by.wialontransport.protocol.wialon.deserializer.impl.parser.MessageParser;
import by.wialontransport.protocol.wialon.deserializer.PackageDeserializer;
import by.wialontransport.protocol.wialon.model.DataPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static by.wialontransport.protocol.wialon.model.DataPackage.PACKAGE_DESCRIPTION_POSTFIX;
import static by.wialontransport.protocol.wialon.model.DataPackage.PACKAGE_DESCRIPTION_PREFIX;
import static java.util.Collections.singletonList;

@RequiredArgsConstructor
@Component
public final class RequestDataPackageDeserializer implements PackageDeserializer {
    private final MessageParser messageDeserializer;

    @Override
    public DataPackage deserialize(String deserialized) {
        final String serializedMessage = deserialized
                .replace(PACKAGE_DESCRIPTION_PREFIX, "")
                .replace(PACKAGE_DESCRIPTION_POSTFIX, "");
        final Message message = this.messageDeserializer.parse(serializedMessage);
        return new DataPackage(singletonList(message));
    }
}
