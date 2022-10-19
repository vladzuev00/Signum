package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.protocol.wialon.connectionmanager.ConnectionManager;
import by.aurorasoft.signum.protocol.wialon.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.wialon.service.AuthorizationDeviceService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Optional.of;

@Component
public final class LoginPackageHandler extends PackageHandler {
    private static final String RESPONSE_SUCCESS_AUTHORIZE = "#AL#1";
    private static final String RESPONSE_FAILURE_AUTHORIZE = "#AL#0";

    private final AuthorizationDeviceService authorizationDeviceService;
    private final ContextManager contextWorker;
    private final ConnectionManager connectionManager;

    public LoginPackageHandler(PingPackageHandler nextHandler, AuthorizationDeviceService authorizationDeviceService,
                               ContextManager contextWorker, ConnectionManager connectionManager) {
        super(LoginPackage.class, nextHandler);
        this.authorizationDeviceService = authorizationDeviceService;
        this.contextWorker = contextWorker;
        this.connectionManager = connectionManager;
    }

    @Override
    protected Optional<String> doHandle(Package requestPackage, ChannelHandlerContext context) {
        final LoginPackage loginPackage = (LoginPackage) requestPackage;
        final Optional<Unit> optionalUnit = this.authorizationDeviceService.authorize(loginPackage);
        if (optionalUnit.isEmpty()) {
            return of(RESPONSE_FAILURE_AUTHORIZE);
        }
        this.contextWorker.putUnit(context, optionalUnit.get());
        this.connectionManager.addContext(context);
        return of(RESPONSE_SUCCESS_AUTHORIZE);
    }
}
