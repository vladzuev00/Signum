package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.protocol.wialon.model.RequestLoginPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.core.service.AuthorizationDeviceService;
import by.aurorasoft.signum.protocol.core.service.CommandSenderService;
import io.netty.channel.ChannelFuture;
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

import static by.aurorasoft.signum.crud.model.dto.Device.Type.TRACKER;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class LoginPackageHandlerTest {

    @Mock
    private AuthorizationDeviceService mockedAuthorizationDeviceService;

    @Mock
    private CommandSenderService mockedCommandSenderService;

    private PackageHandler packageHandler;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Before
    public void initializePackageHandler() {
       this.packageHandler = new LoginPackageHandler(null, this.mockedAuthorizationDeviceService,
               this.mockedCommandSenderService);
    }

    @Test
    public void handlerShouldHandlePackageAndAuthorizationShouldBeSuccess() {
        final RequestLoginPackage givenPackage = new RequestLoginPackage("12345678912345678911", "password");

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Device givenDevice = new Device(255L, givenPackage.getImei(), "448883634", TRACKER);
        when(this.mockedAuthorizationDeviceService.authorize(anyString(), any(ChannelHandlerContext.class)))
                .thenReturn(Optional.of(givenDevice));

        final ChannelFuture givenChannelFuture = mock(ChannelFuture.class);
        when(givenContext.writeAndFlush(any())).thenReturn(givenChannelFuture);

        this.packageHandler.doHandle(givenPackage, givenContext);

        verify(this.mockedAuthorizationDeviceService, times(1))
                .authorize(this.stringArgumentCaptor.capture(), this.contextArgumentCaptor.capture());
        verify(givenContext, times(1)).writeAndFlush(this.stringArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
        assertEquals(List.of(givenPackage.getImei(), "#AL#1"), this.stringArgumentCaptor.getAllValues());
    }

    @Test
    public void handlerShouldHandlePackageAndAuthorizationShouldBeFailure() {
        final RequestLoginPackage givenPackage = new RequestLoginPackage("12345678912345678911", "password");

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        when(this.mockedAuthorizationDeviceService.authorize(anyString(), any(ChannelHandlerContext.class)))
                .thenReturn(empty());

        final ChannelFuture givenChannelFuture = mock(ChannelFuture.class);
        when(givenContext.writeAndFlush(any())).thenReturn(givenChannelFuture);

        this.packageHandler.doHandle(givenPackage, givenContext);

        verify(this.mockedAuthorizationDeviceService, times(1))
                .authorize(this.stringArgumentCaptor.capture(), this.contextArgumentCaptor.capture());
        verify(givenContext, times(1)).writeAndFlush(this.stringArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
        assertEquals(List.of(givenPackage.getImei(), "#AL#0"), this.stringArgumentCaptor.getAllValues());
    }

    @Test(expected = ClassCastException.class)
    public void handlerShouldNotHandlePackageBecauseOfNotSuitableType() {
        final Package givenPackage = new Package() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.packageHandler.doHandle(givenPackage, givenContext);
    }
}
