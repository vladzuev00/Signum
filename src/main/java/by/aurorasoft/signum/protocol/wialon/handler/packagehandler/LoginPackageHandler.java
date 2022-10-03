package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.service.AuthorizationDeviceService;
import org.springframework.stereotype.Component;

@Component
public final class LoginPackageHandler extends PackageHandler {
    private static final String RESPONSE_SUCCESS_AUTHORIZE = "#AL#1";
    private static final String RESPONSE_FAILURE_AUTHORIZE = "#AL#0";

    private final AuthorizationDeviceService authorizationDeviceService;

    public LoginPackageHandler(PingPackageHandler nextHandler, AuthorizationDeviceService authorizationDeviceService) {
        super(LoginPackage.class, nextHandler);
        this.authorizationDeviceService = authorizationDeviceService;
    }

    @Override
    protected String doHandle(Package requestPackage) {
        final LoginPackage loginPackage = (LoginPackage) requestPackage;
        final boolean authorized = this.authorizationDeviceService.authorize(loginPackage);
        return authorized ? RESPONSE_SUCCESS_AUTHORIZE : RESPONSE_FAILURE_AUTHORIZE;
    }
}
