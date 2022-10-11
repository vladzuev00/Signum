package by.aurorasoft.signum.protocol.wialon.model;

import by.aurorasoft.signum.crud.model.dto.Message;

import java.util.List;

public final class DataPackage extends AbstractDataPackage {
    public static final String PACKAGE_PREFIX = "#D#";

    public DataPackage(final List<Message> messages) {
        super(messages);
    }
}
