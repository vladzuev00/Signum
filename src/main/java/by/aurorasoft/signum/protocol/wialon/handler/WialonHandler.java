package by.aurorasoft.signum.protocol.wialon.handler;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.protocol.core.connectionmanager.ConnectionManager;
import by.aurorasoft.signum.protocol.core.exception.AnswerableException;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.PackageHandler;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.StarterPackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

@Slf4j
public final class WialonHandler extends ChannelInboundHandlerAdapter {
    private static final String TEMPLATE_MESSAGE_START_HANDLING_PACKAGE
            = "Start handling inbound package: '%s'.";
    private static final String MESSAGE_ACTIVE_CHANNEL = "New device is connected.";
    private static final String TEMPLATE_MESSAGE_INACTIVE_CHANNEL = "Device with imei '%s' is disconnected.";
    private static final String NOT_DEFINED_DEVICE_IMEI_IN_MESSAGE = "not defined imei";

    private final PackageHandler starterPackageHandler;
    private final ContextManager contextManager;
    private final ConnectionManager connectionManager;

    public WialonHandler(StarterPackageHandler starterPackageHandler, ContextManager contextManager,
                         ConnectionManager connectionManager) {
        this.starterPackageHandler = starterPackageHandler;
        this.contextManager = contextManager;
        this.connectionManager = connectionManager;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object requestObject) {
        final Package requestPackage = (Package) requestObject;
        log.info(format(TEMPLATE_MESSAGE_START_HANDLING_PACKAGE, requestPackage));
        this.starterPackageHandler.handle(requestPackage, context);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable exception) {
        if (exception instanceof DecoderException) {  //exception in decoders are wrapped in DecoderException
            exception = exception.getCause();
        }
        if (exception instanceof AnswerableException) {
            final AnswerableException answerableException = (AnswerableException) exception;
            context.writeAndFlush(answerableException.getAnswerToClient());
        } else {
            context.fireExceptionCaught(exception);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        log.info(MESSAGE_ACTIVE_CHANNEL);
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        final String deviceImei = this.contextManager.findDeviceImei(context);
        log.info(format(TEMPLATE_MESSAGE_INACTIVE_CHANNEL,
                deviceImei != null ? deviceImei : NOT_DEFINED_DEVICE_IMEI_IN_MESSAGE));

        final Device device = this.contextManager.findDevice(context);
        if (device != null) {       //if device was authorized
            this.connectionManager.removeContextByDeviceId(device.getId());
        }
    }
}
