package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.protocol.core.connectionmanager.ConnectionManager;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.core.service.AuthorizationDeviceService;
import by.aurorasoft.signum.protocol.wialon.service.sendcommand.CommandSenderService;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class LoginPackageHandler extends PackageHandler {
    private static final String RESPONSE_SUCCESS_AUTHORIZE = "#AL#1";
    private static final String RESPONSE_FAILURE_AUTHORIZE = "#AL#0";

    private final AuthorizationDeviceService authorizationDeviceService;
    private final ContextManager contextManager;
    private final ConnectionManager connectionManager;

    public LoginPackageHandler(PingPackageHandler nextHandler, AuthorizationDeviceService authorizationDeviceService,
                               ContextManager contextManager, ConnectionManager connectionManager) {
        super(LoginPackage.class, nextHandler);
        this.authorizationDeviceService = authorizationDeviceService;
        this.contextManager = contextManager;
        this.connectionManager = connectionManager;
    }

    @Override
    protected void doHandle(Package requestPackage, ChannelHandlerContext context) {
        final LoginPackage loginPackage = (LoginPackage) requestPackage;
        this.contextManager.putDeviceImei(context, loginPackage.getImei());
        final Optional<Unit> optionalUnit = this.authorizationDeviceService.authorize(loginPackage.getImei());
        optionalUnit.ifPresentOrElse(
                unit -> {
                    this.contextManager.putUnit(context, unit);
                    this.connectionManager.addContext(context);
                    context.writeAndFlush(RESPONSE_SUCCESS_AUTHORIZE)
                            .addListener();
                }
                ,
                () -> context.writeAndFlush(RESPONSE_FAILURE_AUTHORIZE)
        );
    }
}
