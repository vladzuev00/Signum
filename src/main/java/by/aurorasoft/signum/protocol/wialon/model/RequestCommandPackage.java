package by.aurorasoft.signum.protocol.wialon.model;

import lombok.Value;

@Value
public class RequestCommandPackage implements Package {
    public static final String PACKAGE_PREFIX = "#M#";

    String message;
}
