package by.wialontransport.protocol.wialon.model;

import by.wialontransport.entity.Message;

import java.util.List;

public final class DataPackage extends AbstractDataPackage {
    public static final String PACKAGE_DESCRIPTION_PREFIX = "#D#";

    public DataPackage(final List<Message> messages) {
        super(messages);
    }
}
