package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.model.RequestDataPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

import static by.aurorasoft.signum.protocol.wialon.model.RequestDataPackage.PACKAGE_PREFIX;

@Component
public final class DataPackageDeserializer extends AbstractRequestDataPackageDeserializer {
    private static final String RESPONSE_FAILURE_HANDLING = "#AD#0";

    public DataPackageDeserializer(final MessageParser messageParser) {
        super(PACKAGE_PREFIX, messageParser, RESPONSE_FAILURE_HANDLING);
    }

    @Override
    protected Stream<String> splitIntoSubMessages(final String message) {
        return Stream.of(message);
    }

    @Override
    protected Package createPackageBySubMessages(final List<Message> messages) {
        checkContainingOneMessage(messages);
        final Message message = messages.get(0);
        return new RequestDataPackage(message);
    }

    private static void checkContainingOneMessage(final List<Message> messages) {
        if (!isOneMessage(messages)) {
            throw new IllegalArgumentException("Data package should contain only one message");
        }
    }

    private static boolean isOneMessage(final List<Message> messages) {
        return messages.size() == 1;
    }
}
