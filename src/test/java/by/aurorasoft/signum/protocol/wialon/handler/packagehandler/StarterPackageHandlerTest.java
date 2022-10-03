package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public final class StarterPackageHandlerTest {
    private final PackageHandler packageHandler;

    public StarterPackageHandlerTest() {
        this.packageHandler = new StarterPackageHandler(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void handlerShouldNotHandlePackage() {
        final Package givenPackage = new Package() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.packageHandler.doHandle(givenPackage, givenContext);
    }
}
