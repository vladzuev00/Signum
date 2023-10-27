package by.aurorasoft.signum.protocol.wialon.model;

import by.aurorasoft.signum.crud.model.dto.Message;

import static java.util.Collections.singletonList;

public final class RequestDataPackage extends AbstractRequestDataPackage {
    public static final String PACKAGE_PREFIX = "#D#";

    public RequestDataPackage(final Message message) {
        super(singletonList(message));
    }
}
