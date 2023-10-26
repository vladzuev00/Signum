package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.core.exception.AnsweredException;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.exception.NotValidMessageException;
import by.aurorasoft.signum.protocol.wialon.model.BlackBoxPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static by.aurorasoft.signum.protocol.wialon.model.BlackBoxPackage.PACKAGE_PREFIX;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Component
public final class BlackBoxPackageDeserializer extends AbstractDataPackageDeserializer {
    private static final String REGEX_MESSAGES_DELIMITER = "\\|";
    private static final String RESPONSE_FAILURE_HANDLING = "#AB#0";

    private final MessageParser messageParser;

    public BlackBoxPackageDeserializer(final MessageParser messageParser) {
        super(PACKAGE_PREFIX);
        this.messageParser = messageParser;
    }

    @Override
    protected Package deserializeMessage(final String message) {
        try {
            final List<Message> messages = this.parseMessages(message);
            return new BlackBoxPackage(messages);
        } catch (final NotValidMessageException cause) {
            throw new AnsweredException(RESPONSE_FAILURE_HANDLING, cause);
        }
    }

    @Override
    protected Stream<String> splitIntoSubMessages(final String message) {
        final String[] subMessages = message.split(REGEX_MESSAGES_DELIMITER);
        return stream(subMessages);
    }

    @Override
    protected Package createPackage(final List<Message> messages) {
        return new BlackBoxPackage(messages);
    }
}
