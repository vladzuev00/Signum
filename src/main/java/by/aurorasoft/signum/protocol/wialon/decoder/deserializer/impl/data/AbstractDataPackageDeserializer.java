package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.model.Package;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public abstract class AbstractDataPackageDeserializer extends PackageDeserializer {
    private final MessageParser messageParser;

    public AbstractDataPackageDeserializer(final String packagePrefix, final MessageParser messageParser) {
        super(packagePrefix);
        this.messageParser = messageParser;
    }

    @Override
    protected final Package deserializeMessage(final String message) {
        return this.splitIntoSubMessages(message)
                .map(this.messageParser::parse)
                .collect(
                        collectingAndThen(
                                toList(),
                                this::createPackage
                        )
                );
    }

    protected abstract Stream<String> splitIntoSubMessages(final String message);

    protected abstract Package createPackage(final List<Message> messages);
}
