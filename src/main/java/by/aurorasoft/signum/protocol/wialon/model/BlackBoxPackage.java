package by.aurorasoft.signum.protocol.wialon.model;

import by.aurorasoft.signum.dto.MessageDto;

import java.util.List;

public final class BlackBoxPackage extends AbstractDataPackage {
    public static final String PACKAGE_PREFIX = "#B#";

    public BlackBoxPackage(List<MessageDto> messages) {
        super(messages);
    }
}
