package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.service.AuthorizationDeviceService;
import io.netty.channel.ChannelHandlerContext;
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

    private PackageHandler packageHandler;

    @Before
    public void initializePackageHandler() {
        this.packageHandler = new LoginPackageHandler(null, this.mockedService);
    }

    @Test
    public void handlerShouldHandlePackageAndAuthorizationShouldBeSuccess() {
        final Package givenPackage = new LoginPackage("12345678912345678911", "password");
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        when(this.mockedService.authorize(any(LoginPackage.class))).thenReturn(true);

        final String actual = this.packageHandler.doHandle(givenPackage, givenContext);
        final String expected = "#AL#1";
        assertEquals(expected, actual);

        verify(this.mockedService, times(1))
                .authorize(this.loginPackageArgumentCaptor.capture());
        assertSame(givenPackage, this.loginPackageArgumentCaptor.getValue());
    }

    @Test
    public void handlerShouldHandlePackageAndAuthorizationShouldBeFailure() {
        final Package givenPackage = new LoginPackage("12345678912345678911", "password");
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        when(this.mockedService.authorize(any(LoginPackage.class))).thenReturn(false);

        final String actual = this.packageHandler.doHandle(givenPackage, givenContext);
        final String expected = "#AL#0";
        assertEquals(expected, actual);

        verify(this.mockedService, times(1)).authorize(this.loginPackageArgumentCaptor.capture());
        assertSame(givenPackage, this.loginPackageArgumentCaptor.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void handlerShouldNotHandlePackageBecauseOfNotSuitableType() {
        final Package givenPackage = new Package() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.packageHandler.doHandle(givenPackage, givenContext);
    }
}
