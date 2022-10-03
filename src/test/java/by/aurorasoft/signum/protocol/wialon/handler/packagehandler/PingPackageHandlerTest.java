package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.wialon.model.PingPackage;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public final class PingPackageHandlerTest {
    private final PackageHandler packageHandler;

    public PingPackageHandlerTest() {
        this.packageHandler = new PingPackageHandler(null);
    }

    @Test
    public void handlerShouldHandlePackage() {
        final Package givenPackage = new PingPackage();
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final String actual = this.packageHandler.doHandle(givenPackage, givenContext);
        final String expected = "#AP#";
        assertEquals(expected, actual);
    }
}
