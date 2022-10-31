package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.core.service.AuthorizationDeviceService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public final class LoginPackageHandler extends PackageHandler {
    private static final String RESPONSE_SUCCESS_AUTHORIZE = "#AL#1";
    private static final String RESPONSE_FAILURE_AUTHORIZE = "#AL#0";

    private final AuthorizationDeviceService authorizationDeviceService;

    public LoginPackageHandler(PingPackageHandler nextHandler,
                               AuthorizationDeviceService authorizationDeviceService) {
        super(LoginPackage.class, nextHandler);
        this.authorizationDeviceService = authorizationDeviceService;
    }

    @Override
    protected void doHandle(Package requestPackage, ChannelHandlerContext context) {
        final LoginPackage loginPackage = (LoginPackage) requestPackage;
        final boolean successAuthorization = this.authorizationDeviceService
                .authorize(context, loginPackage.getImei());
        final String response = successAuthorization ? RESPONSE_SUCCESS_AUTHORIZE : RESPONSE_FAILURE_AUTHORIZE;
        context.writeAndFlush(response);
    }
}
