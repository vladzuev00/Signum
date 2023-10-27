package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data.DataPackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.wialon.model.RequestPingPackage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

@Component
public final class PingPackageHandler extends PackageHandler {
    private static final String RESPONSE_PACKAGE = "#AP#";

    public PingPackageHandler(DataPackageHandler nextHandler) {
        super(RequestPingPackage.class, nextHandler);
    }

    @Override
    protected void doHandle(Package requestPackage, ChannelHandlerContext context) {
        context.writeAndFlush(RESPONSE_PACKAGE);
    }
}
