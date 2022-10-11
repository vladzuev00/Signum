package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.model.BlackBoxPackage;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer.removePrefix;
import static by.aurorasoft.signum.protocol.wialon.model.BlackBoxPackage.PACKAGE_PREFIX;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
public final class BlackBoxPackageDeserializer implements PackageDeserializer {
    private static final String REGEX_MESSAGES_DELIMITER = "\\|";

    private final MessageParser messageParser;

    @Override
    public BlackBoxPackage deserialize(String deserialized) {
        final String message = removePrefix(deserialized, PACKAGE_PREFIX);
        final String[] serializedMessages = message.split(REGEX_MESSAGES_DELIMITER);
        final List<Message> messages = stream(serializedMessages)
                .map(this.messageParser::parse)
                .collect(toList());
        return new BlackBoxPackage(messages);
    }
}
