package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.protocol.wialon.handler.contextworker.ContextWorker;
import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.wialon.service.AuthorizationDeviceService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class LoginPackageHandler extends PackageHandler {
    private static final String RESPONSE_SUCCESS_AUTHORIZE = "#AL#1";
    private static final String RESPONSE_FAILURE_AUTHORIZE = "#AL#0";

    private final AuthorizationDeviceService authorizationDeviceService;
    private final ContextWorker contextWorker;

    public LoginPackageHandler(PingPackageHandler nextHandler, AuthorizationDeviceService authorizationDeviceService,
                               ContextWorker contextWorker) {
        super(LoginPackage.class, nextHandler);
        this.authorizationDeviceService = authorizationDeviceService;
        this.contextWorker = contextWorker;
    }

    @Override
    protected String doHandle(Package requestPackage, ChannelHandlerContext context) {
        final LoginPackage loginPackage = (LoginPackage) requestPackage;
        final Optional<Unit> optionalUnit = this.authorizationDeviceService.authorize(loginPackage);
        if (optionalUnit.isEmpty()) {
            return RESPONSE_FAILURE_AUTHORIZE;
        }
        this.contextWorker.putUnit(context, optionalUnit.get());
        return RESPONSE_SUCCESS_AUTHORIZE;
    }
}
