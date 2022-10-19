package by.aurorasoft.signum.protocol.wialon.handler;

import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.protocol.wialon.contextmanager.ContextManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

@Slf4j
public final class ExceptionHandler extends ChannelInboundHandlerAdapter {
    private static final String TEMPLATE_MESSAGE_EXCEPTION_CAUGHT = "During working with connection to tracker '%s' "
            + "was arisen exception: %s.";

    private final ContextManager contextWorker;

    public ExceptionHandler(ContextManager contextWorker) {
        this.contextWorker = contextWorker;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable exception) {
        final Unit unit = this.contextWorker.findUnit(context);
        log.error(format(TEMPLATE_MESSAGE_EXCEPTION_CAUGHT, unit.getTracker(), exception.getMessage()));
        exception.printStackTrace();
        context.close();
    }
}
