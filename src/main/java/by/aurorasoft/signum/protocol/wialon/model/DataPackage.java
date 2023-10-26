package by.aurorasoft.signum.protocol.wialon.model;

import by.aurorasoft.signum.crud.model.dto.Message;

import static java.util.Collections.singletonList;

public final class DataPackage extends AbstractDataPackage {
    public static final String PACKAGE_PREFIX = "#D#";

    public DataPackage(final Message message) {
        super(singletonList(message));
    }
}
