package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

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

import static by.aurorasoft.signum.protocol.wialon.model.BlackBoxPackage.PACKAGE_PREFIX;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Component
public final class BlackBoxPackageDeserializer extends PackageDeserializer {
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

    private List<Message> parseMessages(final String message) {
        final String[] serializedMessages = message.split(REGEX_MESSAGES_DELIMITER);
        return stream(serializedMessages)
                .map(this.messageParser::parse)
                .toList();
    }
}
