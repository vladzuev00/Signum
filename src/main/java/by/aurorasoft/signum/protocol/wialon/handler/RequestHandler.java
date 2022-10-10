package by.aurorasoft.signum.protocol.wialon.handler;

import by.aurorasoft.signum.protocol.wialon.handler.contextworker.ContextWorker;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.PackageHandler;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.StarterPackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
@Slf4j
public final class RequestHandler extends ChannelInboundHandlerAdapter {
    private static final String MESSAGE_ACTIVE_CHANNEL = "Tracker is connected.";
    private static final String TEMPLATE_MESSAGE_INACTIVE_CHANNEL = "Tracker %s is disconnected";
    private static final String TEMPLATE_MESSAGE_EXCEPTION_CAUGHT = "During working with connection to tracker '%s' "
            + "was arisen exception: %s.";

    private final PackageHandler starterPackageHandler;
    private final ContextWorker trackerImeiFounder;

    public RequestHandler(StarterPackageHandler starterPackageHandler, ContextWorker trackerImeiFounder) {
        this.starterPackageHandler = starterPackageHandler;
        this.trackerImeiFounder = trackerImeiFounder;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object requestObject) {
        final Package requestPackage = (Package) requestObject;
        final String responsePackage = this.starterPackageHandler.handle(requestPackage, context);
        context.writeAndFlush(responsePackage);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        log.info(MESSAGE_ACTIVE_CHANNEL);
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        final String trackerImei = this.trackerImeiFounder.findUnit(context);
        log.info(format(TEMPLATE_MESSAGE_INACTIVE_CHANNEL, trackerImei));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        final String trackerImei = this.trackerImeiFounder.findUnit(context);
        log.info(format(TEMPLATE_MESSAGE_EXCEPTION_CAUGHT, trackerImei, cause.getMessage()));
    }
}
