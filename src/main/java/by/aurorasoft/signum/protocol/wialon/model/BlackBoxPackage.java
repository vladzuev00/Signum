package by.aurorasoft.signum.protocol.wialon.model;

import by.aurorasoft.signum.entity.MessageEntity;

import java.util.List;

public final class BlackBoxPackage extends AbstractDataPackage {
    public static final String PACKAGE_PREFIX = "#B#";

    public BlackBoxPackage(List<MessageEntity> messages) {
        super(messages);
    }
}
