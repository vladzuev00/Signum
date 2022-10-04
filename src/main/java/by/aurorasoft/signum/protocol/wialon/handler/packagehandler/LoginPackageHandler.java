package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.service.AuthorizationDeviceService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public final class LoginPackageHandler extends PackageHandler {
    private static final String RESPONSE_SUCCESS_AUTHORIZE = "#AL#1";
    private static final String RESPONSE_FAILURE_AUTHORIZE = "#AL#0";

    private final AuthorizationDeviceService authorizationDeviceService;

    public LoginPackageHandler(PingPackageHandler nextHandler, AuthorizationDeviceService authorizationDeviceService) {
        super(LoginPackage.class, nextHandler);
        this.authorizationDeviceService = authorizationDeviceService;
    }

    @Override
    protected String doHandle(Package requestPackage, ChannelHandlerContext context) {
        final LoginPackage loginPackage = (LoginPackage) requestPackage;
        final boolean authorized = this.authorizationDeviceService.authorize(loginPackage);
        putImeiAttributeInChannel(loginPackage.getImei(), context);
        return authorized ? RESPONSE_SUCCESS_AUTHORIZE : RESPONSE_FAILURE_AUTHORIZE;
    }

    private static void putImeiAttributeInChannel(String imei, ChannelHandlerContext context) {
        final Channel channel = context.channel();
        final Attribute<String> imeiAttribute = channel.attr(CHANNEL_ATTRIBUTE_KEY_IMEI);
        imeiAttribute.set(imei);
    }
}
