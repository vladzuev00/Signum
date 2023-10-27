package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.core.exception.AnsweredException;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data.parser.exception.NotValidMessageException;
import by.aurorasoft.signum.protocol.wialon.model.AbstractRequestDataPackage;
import by.aurorasoft.signum.protocol.wialon.model.AbstractResponseDataPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public abstract class AbstractRequestDataPackageDeserializer extends PackageDeserializer {
    private final MessageParser messageParser;
    private final ResponseDataPackageFactory responseDataPackageFactory;

    public AbstractRequestDataPackageDeserializer(final String packagePrefix,
                                                  final MessageParser messageParser,
                                                  final ResponseDataPackageFactory responseDataPackageFactory) {
        super(packagePrefix);
        this.messageParser = messageParser;
        this.responseDataPackageFactory = responseDataPackageFactory;
    }

    @Override
    protected final AbstractRequestDataPackage deserializeMessage(final String message) {
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

    protected abstract AbstractRequestDataPackage createPackageBySubMessages(final List<Message> messages);

    private Message parseSubMessage(final String message) {
        try {
            return this.messageParser.parse(message);
        } catch (final NotValidMessageException cause) {
            return this.throwAnsweredException(cause);
        }
    }

    private Message throwAnsweredException(final NotValidMessageException cause) {
        final AbstractResponseDataPackage exceptionAnswer = this.responseDataPackageFactory.create(0);
        throw new AnsweredException(exceptionAnswer, cause);
    }

    @FunctionalInterface
    protected interface ResponseDataPackageFactory {
        AbstractResponseDataPackage create(final int countFixedMessages);
    }
}
