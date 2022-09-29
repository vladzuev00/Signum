package by.wialontransport.protocol.wialon.model;

import by.wialontransport.entity.Message;

import java.util.List;

public final class BlackBoxPackage extends AbstractDataPackage {
    public static final String PACKAGE_DESCRIPTION_PREFIX = "#B#";

    public BlackBoxPackage(List<Message> messages) {
        super(messages);
    }
}
