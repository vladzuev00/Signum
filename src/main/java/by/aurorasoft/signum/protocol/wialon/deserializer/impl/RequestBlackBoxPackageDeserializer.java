package by.aurorasoft.signum.protocol.wialon.deserializer.impl;

import by.aurorasoft.signum.entity.Message;
import by.aurorasoft.signum.protocol.wialon.deserializer.impl.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.model.BlackBoxPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.wialon.deserializer.PackageDeserializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
                .replace(BlackBoxPackage.PACKAGE_DESCRIPTION_PREFIX, "")
                .replace(Package.PACKAGE_DESCRIPTION_POSTFIX, "");
        final String[] serializedMessages = message.split(REGEX_MESSAGES_DELIMITER);
        final List<Message> messages = stream(serializedMessages)
                .map(this.messageDeserializer::parse)
                .collect(toList());
        return new BlackBoxPackage(messages);
    }
}
