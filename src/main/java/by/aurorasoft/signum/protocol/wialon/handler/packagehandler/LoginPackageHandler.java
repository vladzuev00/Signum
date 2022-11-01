package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.core.service.AuthorizationDeviceService;
import by.aurorasoft.signum.protocol.core.service.CommandSenderService;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class LoginPackageHandler extends PackageHandler {
    private static final String RESPONSE_SUCCESS_AUTHORIZE = "#AL#1";
    private static final String RESPONSE_FAILURE_AUTHORIZE = "#AL#0";

    private final AuthorizationDeviceService authorizationDeviceService;
    private final CommandSenderService commandSenderService;

    public LoginPackageHandler(PingPackageHandler nextHandler,
                               AuthorizationDeviceService authorizationDeviceService,
                               CommandSenderService commandSenderService) {
        super(LoginPackage.class, nextHandler);
        this.authorizationDeviceService = authorizationDeviceService;
        this.commandSenderService = commandSenderService;
    }

    @Override
    protected void doHandle(Package requestPackage, ChannelHandlerContext context) {
        final LoginPackage loginPackage = (LoginPackage) requestPackage;
        final Optional<Device> optionalDevice = this.authorizationDeviceService
                .authorize(loginPackage.getImei(), context);
        final String response = optionalDevice.isPresent()
                ? RESPONSE_SUCCESS_AUTHORIZE
                : RESPONSE_FAILURE_AUTHORIZE;
        context.writeAndFlush(response)
                .addListener((ChannelFutureListener)
                        future -> optionalDevice.ifPresent(this.commandSenderService::resendCommands));
    }
}
