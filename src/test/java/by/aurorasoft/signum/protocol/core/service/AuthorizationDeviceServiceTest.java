package by.aurorasoft.signum.protocol.core.service;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.service.DeviceService;
import by.aurorasoft.signum.protocol.core.connectionmanager.ConnectionManager;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
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

import static by.aurorasoft.signum.crud.model.entity.DeviceEntity.Type.TRACKER;
import static java.util.Optional.empty;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class AuthorizationDeviceServiceTest {

    @Mock
    private ContextManager mockedContextManager;

    @Mock
    private DeviceService mockedDeviceService;

    @Mock
    private ConnectionManager mockedConnectionManager;

    private AuthorizationDeviceService authorizationDeviceService;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<Device> deviceArgumentCaptor;

    @Before
    public void initializeAuthorizationDeviceService() {
        this.authorizationDeviceService = new AuthorizationDeviceService(
                this.mockedContextManager, this.mockedDeviceService, this.mockedConnectionManager);
    }

    @Test
    public void deviceShouldAuthorize() {
       final Device givenDevice = new Device(255L, "11111222223333344444", "334445566", TRACKER);

       when(this.mockedDeviceService.findByImei(anyString())).thenReturn(Optional.of(givenDevice));

       final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
       final Optional<Device> optionalActual = this.authorizationDeviceService
               .authorize(givenContext, givenDevice.getImei());
       assertTrue(optionalActual.isPresent());
       final Device actual = optionalActual.get();
       assertEquals(givenDevice, actual);

       verify(this.mockedContextManager, times(1))
               .putDeviceImei(this.contextArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
       verify(this.mockedDeviceService, times(1))
               .findByImei(this.stringArgumentCaptor.capture());
       verify(this.mockedContextManager, times(1))
               .putDevice(this.contextArgumentCaptor.capture(), this.deviceArgumentCaptor.capture());
       verify(this.mockedConnectionManager, times(1))
               .addContext(this.contextArgumentCaptor.capture());

       assertEquals(List.of(givenContext, givenContext, givenContext), this.contextArgumentCaptor.getAllValues());
       assertEquals(List.of(givenDevice.getImei(), givenDevice.getImei()), this.stringArgumentCaptor.getAllValues());
       assertEquals(givenDevice, this.deviceArgumentCaptor.getValue());
    }

    @Test
    public void deviceShouldNotAuthorize() {
        when(this.mockedDeviceService.findByImei(anyString())).thenReturn(empty());

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final String givenImei = "11111222223333344444";
        final Optional<Device> optionalDevice = this.authorizationDeviceService.authorize(givenContext, givenImei);
        assertTrue(optionalDevice.isEmpty());

        verify(this.mockedContextManager, times(1))
                .putDeviceImei(this.contextArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
        verify(this.mockedDeviceService, times(1))
                .findByImei(this.stringArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
        assertEquals(List.of(givenImei, givenImei), this.stringArgumentCaptor.getAllValues());
    }
}
