package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.exception.NotValidMessageException;
import by.aurorasoft.signum.protocol.core.exception.AnswerableException;
import by.aurorasoft.signum.protocol.wialon.model.DataPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer.removePrefix;
import static by.aurorasoft.signum.protocol.wialon.model.DataPackage.PACKAGE_PREFIX;
import static java.util.Collections.singletonList;

@RequiredArgsConstructor
@Component
public final class DataPackageDeserializer implements PackageDeserializer {
    private static final String RESPONSE_FAILURE_HANDLING = "#AD#0";
    private final MessageParser messageParser;

    @Override
    public DataPackage deserialize(String deserialized) {
        try {
            final String serializedMessage = removePrefix(deserialized, PACKAGE_PREFIX);
            final Message message = this.messageParser.parse(serializedMessage);
            return new DataPackage(singletonList(message));
        } catch (final NotValidMessageException cause) {
            throw new AnswerableException(RESPONSE_FAILURE_HANDLING, cause);
        }
    }
}
