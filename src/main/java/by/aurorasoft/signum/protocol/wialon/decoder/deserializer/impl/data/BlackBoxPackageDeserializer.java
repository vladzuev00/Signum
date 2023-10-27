package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.model.RequestBlackBoxPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

import static by.aurorasoft.signum.protocol.wialon.model.RequestBlackBoxPackage.PACKAGE_PREFIX;
import static java.util.Arrays.stream;

@Component
public final class BlackBoxPackageDeserializer extends AbstractRequestDataPackageDeserializer {
    private static final String REGEX_MESSAGES_DELIMITER = "\\|";
    private static final String RESPONSE_FAILURE_HANDLING = "#AB#0";

    public BlackBoxPackageDeserializer(final MessageParser messageParser) {
        super(PACKAGE_PREFIX, messageParser, RESPONSE_FAILURE_HANDLING);
    }

    @Override
    protected Stream<String> splitIntoSubMessages(final String message) {
        final String[] subMessages = message.split(REGEX_MESSAGES_DELIMITER);
        return stream(subMessages);
    }

    @Override
    protected Package createPackageBySubMessages(final List<Message> messages) {
        return new RequestBlackBoxPackage(messages);
    }
}
