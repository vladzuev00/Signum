package by.aurorasoft.signum.protocol.wialon.model;

import by.aurorasoft.signum.entity.Message;

import java.util.List;

public final class BlackBoxPackage extends AbstractDataPackage {
    public static final String PACKAGE_PREFIX = "#B#";

    public BlackBoxPackage(List<Message> messages) {
        super(messages);
    }
}
