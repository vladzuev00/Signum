package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public final class FinisherPackageHandlerTest {

    private final FinisherPackageHandler packageHandler;

    public FinisherPackageHandlerTest() {
        this.packageHandler = new FinisherPackageHandler();
    }

    @SuppressWarnings("all")
    @Test(expected = UnsupportedOperationException.class)
    public void handlerShouldNotHandlePackage() {
        final Package givenPackage = new Package() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.packageHandler.doHandle(givenPackage, givenContext);
    }
}
