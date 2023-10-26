package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.core.exception.AnsweredException;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.exception.NotValidMessageException;
import by.aurorasoft.signum.protocol.wialon.model.Package;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public abstract class AbstractDataPackageDeserializer extends PackageDeserializer {
    private final MessageParser messageParser;
    private final String responseFailureHandling;

    public AbstractDataPackageDeserializer(final String packagePrefix,
                                           final MessageParser messageParser,
                                           final String responseFailureHandling) {
        super(packagePrefix);
        this.messageParser = messageParser;
        this.responseFailureHandling = responseFailureHandling;
    }

    @Override
    protected final Package deserializeMessage(final String message) {
        return this.splitIntoSubMessages(message)
                .map(this::parseSubMessage)
                .collect(
                        collectingAndThen(
                                toList(),
                                this::createPackageBySubMessages
                        )
                );
    }

    protected abstract Stream<String> splitIntoSubMessages(final String message);

    protected abstract Package createPackageBySubMessages(final List<Message> messages);

    private Message parseSubMessage(final String message) {
        try {
            return this.messageParser.parse(message);
        } catch (final NotValidMessageException cause) {
            throw new AnsweredException(this.responseFailureHandling, cause);
        }
    }
}
