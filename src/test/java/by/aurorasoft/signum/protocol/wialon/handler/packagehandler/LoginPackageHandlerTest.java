package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.service.AuthorizationDeviceService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class LoginPackageHandlerTest {

    @Mock
    private AuthorizationDeviceService mockedService;

    @Captor
    private ArgumentCaptor<LoginPackage> loginPackageArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    private PackageHandler packageHandler;

    @Before
    public void initializePackageHandler() {
        this.packageHandler = new LoginPackageHandler(null, this.mockedService);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void handlerShouldHandlePackageAndAuthorizationShouldBeSuccess() {
        final String givenImei = "12345678912345678911";
        final Package givenPackage = new LoginPackage(givenImei, "password");

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<String> givenImeiAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenImeiAttribute);

        when(this.mockedService.authorize(any(LoginPackage.class))).thenReturn(true);

        final String actual = this.packageHandler.doHandle(givenPackage, givenContext);
        final String expected = "#AL#1";
        assertEquals(expected, actual);

        verify(this.mockedService, times(1))
                .authorize(this.loginPackageArgumentCaptor.capture());
        verify(givenImeiAttribute, times(1)).set(this.stringArgumentCaptor.capture());

        assertSame(givenPackage, this.loginPackageArgumentCaptor.getValue());
        assertSame(givenImei, this.stringArgumentCaptor.getValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void handlerShouldHandlePackageAndAuthorizationShouldBeFailure() {
        final String givenImei = "12345678912345678911";
        final Package givenPackage = new LoginPackage(givenImei, "password");

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<String> givenImeiAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenImeiAttribute);

        when(this.mockedService.authorize(any(LoginPackage.class))).thenReturn(false);

        final String actual = this.packageHandler.doHandle(givenPackage, givenContext);
        final String expected = "#AL#0";
        assertEquals(expected, actual);

        verify(this.mockedService, times(1)).authorize(this.loginPackageArgumentCaptor.capture());
        verify(givenImeiAttribute, times(1)).set(this.stringArgumentCaptor.capture());

        assertSame(givenPackage, this.loginPackageArgumentCaptor.getValue());
        assertSame(givenImei, this.stringArgumentCaptor.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void handlerShouldNotHandlePackageBecauseOfNotSuitableType() {
        final Package givenPackage = new Package() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.packageHandler.doHandle(givenPackage, givenContext);
    }
}
