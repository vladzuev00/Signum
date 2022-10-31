package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.wialon.model.PingPackage;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class PingPackageHandlerTest {
    private final PackageHandler packageHandler;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    public PingPackageHandlerTest() {
        this.packageHandler = new PingPackageHandler(null);
    }

    @Test
    public void handlerShouldHandlePackage() {
        final Package givenPackage = new PingPackage();
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.packageHandler.doHandle(givenPackage, givenContext);

        verify(givenContext, times(1))
                .writeAndFlush(this.stringArgumentCaptor.capture());
        assertEquals("#AP#", this.stringArgumentCaptor.getValue());
    }
}
