package by.aurorasoft.signum.protocol.wialon.model;

import by.aurorasoft.signum.crud.model.dto.Message;

import java.util.List;

public final class RequestBlackBoxPackage extends AbstractRequestDataPackage {
    public static final String PACKAGE_PREFIX = "#B#";

    public RequestBlackBoxPackage(List<Message> messages) {
        super(messages);
    }
}
