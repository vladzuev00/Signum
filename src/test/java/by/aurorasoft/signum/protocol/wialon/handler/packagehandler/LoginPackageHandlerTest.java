package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.protocol.core.connectionmanager.ConnectionManager;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.core.service.AuthorizationDeviceService;
import by.aurorasoft.signum.protocol.wialon.service.sendcommand.CommandSenderService;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class LoginPackageHandlerTest {

    @Mock
    private AuthorizationDeviceService mockedAuthorizationDeviceService;

    @Mock
    private ContextManager mockedContextManager;

    @Mock
    private ConnectionManager mockedConnectionManager;

    @Mock
    private CommandSenderService mockedCommandSenderService;

    private PackageHandler packageHandler;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Captor
    private ArgumentCaptor<Unit> unitArgumentCaptor;

    @Before
    public void initializePackageHandler() {
       this.packageHandler = new LoginPackageHandler(null, this.mockedAuthorizationDeviceService,
               this.mockedContextManager, this.mockedConnectionManager, this.mockedCommandSenderService);
    }

    @Test
    public void handlerShouldHandlePackageAndAuthorizationShouldBeSuccess() {
        final LoginPackage givenPackage = new LoginPackage("12345678912345678911", "password");

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Unit givenUnit = mock(Unit.class);
        when(this.mockedAuthorizationDeviceService.authorize(anyString())).thenReturn(Optional.of(givenUnit));

        final ChannelFuture givenChannelFuture = mock(ChannelFuture.class);
        when(givenContext.writeAndFlush(any())).thenReturn(givenChannelFuture);

        this.packageHandler.doHandle(givenPackage, givenContext);

        verify(this.mockedContextManager, times(1))
                .putDeviceImei(this.contextArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
        verify(this.mockedAuthorizationDeviceService, times(1))
                .authorize(this.stringArgumentCaptor.capture());
        verify(this.mockedContextManager, times(1))
                .putUnit(this.contextArgumentCaptor.capture(), this.unitArgumentCaptor.capture());
        verify(this.mockedConnectionManager, times(1))
                .addContext(this.contextArgumentCaptor.capture());
        verify(givenContext, times(1)).writeAndFlush(this.stringArgumentCaptor.capture());

        assertEquals(List.of(givenPackage.getImei(), givenPackage.getImei(), "#AL#1"),
                this.stringArgumentCaptor.getAllValues());
        assertEquals(List.of(givenContext, givenContext, givenContext), this.contextArgumentCaptor.getAllValues());
        assertSame(givenUnit, this.unitArgumentCaptor.getValue());
    }

    @Test
    public void handlerShouldHandlePackageAndAuthorizationShouldBeFailure() {
        final LoginPackage givenPackage = new LoginPackage("12345678912345678911", "password");

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        when(this.mockedAuthorizationDeviceService.authorize(anyString())).thenReturn(empty());

        this.packageHandler.doHandle(givenPackage, givenContext);

        verify(this.mockedContextManager, times(1))
                .putDeviceImei(this.contextArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
        verify(this.mockedAuthorizationDeviceService, times(1))
                .authorize(this.stringArgumentCaptor.capture());
        verify(givenContext, times(1)).writeAndFlush(this.stringArgumentCaptor.capture());

        assertEquals(List.of(givenPackage.getImei(), givenPackage.getImei(), "#AL#0"),
                this.stringArgumentCaptor.getAllValues());
        assertSame(givenContext, this.contextArgumentCaptor.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void handlerShouldNotHandlePackageBecauseOfNotSuitableType() {
        final Package givenPackage = new Package() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.packageHandler.doHandle(givenPackage, givenContext);
    }
}
