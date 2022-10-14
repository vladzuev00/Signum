package by.aurorasoft.signum.protocol.wialon.handler;

import by.aurorasoft.signum.protocol.wialon.exception.AnswerableException;
import by.aurorasoft.signum.protocol.wialon.handler.contextworker.ContextWorker;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.PackageHandler;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.StarterPackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RequestHandler extends ChannelInboundHandlerAdapter {
    private static final String MESSAGE_ACTIVE_CHANNEL = "Tracker is connected.";

    private final PackageHandler starterPackageHandler;
    private final ContextWorker contextWorker;

    public RequestHandler(StarterPackageHandler starterPackageHandler, ContextWorker contextWorker) {
        this.starterPackageHandler = starterPackageHandler;
        this.contextWorker = contextWorker;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object requestObject) {
        final Package requestPackage = (Package) requestObject;
        final String responsePackage = this.starterPackageHandler.handle(requestPackage, context);
        context.writeAndFlush(responsePackage);
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
