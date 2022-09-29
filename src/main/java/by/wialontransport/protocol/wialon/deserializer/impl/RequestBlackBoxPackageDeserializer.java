package by.wialontransport.protocol.wialon.deserializer.impl;

import by.wialontransport.entity.Message;
import by.wialontransport.protocol.wialon.deserializer.impl.parser.MessageParser;
import by.wialontransport.protocol.wialon.deserializer.PackageDeserializer;
import by.wialontransport.protocol.wialon.model.BlackBoxPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.wialontransport.protocol.wialon.model.BlackBoxPackage.PACKAGE_DESCRIPTION_POSTFIX;
import static by.wialontransport.protocol.wialon.model.BlackBoxPackage.PACKAGE_DESCRIPTION_PREFIX;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
public final class RequestBlackBoxPackageDeserializer implements PackageDeserializer {
    private static final String REGEX_MESSAGES_DELIMITER = "\\|";

    private final MessageParser messageDeserializer;

    @Override
    public BlackBoxPackage deserialize(String deserialized) {
        final String message = deserialized
                .replace(PACKAGE_DESCRIPTION_PREFIX, "")
                .replace(PACKAGE_DESCRIPTION_POSTFIX, "");
        final String[] serializedMessages = message.split(REGEX_MESSAGES_DELIMITER);
        final List<Message> messages = stream(serializedMessages)
                .map(this.messageDeserializer::parse)
                .collect(toList());
        return new BlackBoxPackage(messages);
    }
}
