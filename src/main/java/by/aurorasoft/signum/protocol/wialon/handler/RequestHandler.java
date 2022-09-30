package by.aurorasoft.signum.protocol.wialon.handler;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

@Component
public final class RequestHandler extends ChannelInboundHandlerAdapter {
    private final PackageHandler starterPackageHandler;

    public RequestHandler(StarterPackageHandler starterPackageHandler) {
        this.starterPackageHandler = starterPackageHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object requestObject) {
        final Package requestPackage = (Package) requestObject;
        this.starterPackageHandler.doHandle(requestPackage);
    }
}
