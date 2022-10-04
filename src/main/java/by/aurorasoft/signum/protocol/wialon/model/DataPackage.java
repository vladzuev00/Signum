package by.aurorasoft.signum.protocol.wialon.model;

import by.aurorasoft.signum.entity.MessageEntity;

import java.util.List;

public final class DataPackage extends AbstractDataPackage {
    public static final String PACKAGE_PREFIX = "#D#";

    public DataPackage(final List<MessageEntity> messages) {
        super(messages);
    }
}
