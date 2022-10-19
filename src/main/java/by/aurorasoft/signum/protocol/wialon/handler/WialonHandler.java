package by.aurorasoft.signum.protocol.wialon.handler;

import by.aurorasoft.signum.protocol.wialon.exception.AnswerableException;
import by.aurorasoft.signum.protocol.wialon.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.PackageHandler;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.StarterPackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static java.lang.String.format;

@Slf4j
public final class WialonHandler extends ChannelInboundHandlerAdapter {
    private static final String TEMPLATE_MESSAGE_START_HANDLING_PACKAGE
            = "Start handling inbound package: '%s'.";
    private static final String MESSAGE_ACTIVE_CHANNEL = "New tracker is connected.";

    private final PackageHandler starterPackageHandler;
    private final ContextManager contextWorker;

    public WialonHandler(StarterPackageHandler starterPackageHandler, ContextManager contextWorker) {
        this.starterPackageHandler = starterPackageHandler;
        this.contextWorker = contextWorker;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object requestObject) {
        final Package requestPackage = (Package) requestObject;
        log.info(format(TEMPLATE_MESSAGE_START_HANDLING_PACKAGE, requestPackage));
        final Optional<String> optionalResponsePackage = this.starterPackageHandler.handle(requestPackage, context);
        optionalResponsePackage.ifPresent(context::writeAndFlush);
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

    }
}
