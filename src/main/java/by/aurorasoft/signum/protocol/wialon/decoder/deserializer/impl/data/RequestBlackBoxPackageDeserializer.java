package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.model.RequestBlackBoxPackage;
import by.aurorasoft.signum.protocol.wialon.model.ResponseBlackBoxPackage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

import static by.aurorasoft.signum.protocol.wialon.model.RequestBlackBoxPackage.PACKAGE_PREFIX;
import static java.util.Arrays.stream;

@Component
public final class RequestBlackBoxPackageDeserializer extends AbstractRequestDataPackageDeserializer {
    private static final String REGEX_MESSAGES_DELIMITER = "\\|";

    public RequestBlackBoxPackageDeserializer(final MessageParser messageParser) {
        super(PACKAGE_PREFIX, messageParser, ResponseBlackBoxPackage::new);
    }

    @Override
    protected Stream<String> splitIntoSubMessages(final String message) {
        final String[] subMessages = message.split(REGEX_MESSAGES_DELIMITER);
        return stream(subMessages);
    }

    @Override
    protected RequestBlackBoxPackage createPackageBySubMessages(final List<Message> messages) {
        return new RequestBlackBoxPackage(messages);
    }
}
