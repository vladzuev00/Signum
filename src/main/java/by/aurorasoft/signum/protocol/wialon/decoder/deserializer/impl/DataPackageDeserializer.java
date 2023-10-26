package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.core.exception.AnsweredException;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.exception.NotValidMessageException;
import by.aurorasoft.signum.protocol.wialon.model.DataPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.protocol.wialon.model.DataPackage.PACKAGE_PREFIX;
import static java.util.Collections.singletonList;

@Component
public final class DataPackageDeserializer extends PackageDeserializer {
    private static final String RESPONSE_FAILURE_HANDLING = "#AD#0";

    private final MessageParser messageParser;

    public DataPackageDeserializer(final MessageParser messageParser) {
        super(PACKAGE_PREFIX);
        this.messageParser = messageParser;
    }

    @Override
    protected Package deserializeMessage(final String serializedMessage) {
        try {
            final Message message = this.messageParser.parse(serializedMessage);
            return new DataPackage(singletonList(message));
        } catch (final NotValidMessageException cause) {
            throw new AnsweredException(RESPONSE_FAILURE_HANDLING, cause);
        }
    }
}
