package by.aurorasoft.signum.protocol.wialon.model;

import lombok.Value;

@Value
public class LoginPackage implements Package {
    public static final String PACKAGE_PREFIX = "#L#";

    String imei;
    String password;
}
