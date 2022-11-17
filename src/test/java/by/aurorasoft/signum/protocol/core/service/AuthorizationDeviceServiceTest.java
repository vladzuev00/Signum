package by.aurorasoft.signum.protocol.core.service;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.service.DeviceService;
import by.aurorasoft.signum.crud.service.MessageService;
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

import static by.aurorasoft.signum.crud.model.dto.Device.Type.TRACKER;
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

    @Mock
    private MessageService mockedMessageService;

    private AuthorizationDeviceService authorizationDeviceService;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<Device> deviceArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<Message> messageArgumentCaptor;

    @Before
    public void initializeAuthorizationDeviceService() {
        this.authorizationDeviceService = new AuthorizationDeviceService(
                this.mockedContextManager, this.mockedDeviceService, this.mockedConnectionManager,
                this.mockedMessageService);
    }

    @Test
    public void deviceShouldBeAuthorizedAndPreviousMessageShouldBePutInContext() {
        final Device givenDevice = new Device(255L, "11111222223333344444", "334445566", TRACKER);
        when(this.mockedDeviceService.findByImei(anyString())).thenReturn(Optional.of(givenDevice));

        final Message givenLastMessage = mock(Message.class);
        when(this.mockedMessageService.findLastMessage(anyLong())).thenReturn(Optional.of(givenLastMessage));

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Optional<Device> optionalActual = this.authorizationDeviceService
                .authorize(givenDevice.getImei(), givenContext);
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
                .add(this.contextArgumentCaptor.capture());
        verify(this.mockedMessageService, times(1))
                .findLastMessage(this.longArgumentCaptor.capture());
        verify(this.mockedContextManager, times(1))
                .putLastMessage(this.contextArgumentCaptor.capture(), this.messageArgumentCaptor.capture());

        assertEquals(List.of(givenContext, givenContext, givenContext, givenContext),
                this.contextArgumentCaptor.getAllValues());
        assertEquals(List.of(givenDevice.getImei(), givenDevice.getImei()),
                this.stringArgumentCaptor.getAllValues());
        assertEquals(givenDevice, this.deviceArgumentCaptor.getValue());
        assertEquals(givenDevice.getId(), this.longArgumentCaptor.getValue());
        assertEquals(givenLastMessage, this.messageArgumentCaptor.getValue());
    }

    @Test
    public void deviceShouldBeAuthorizedAndPreviousMessageShouldNotBePutInContextBecauseOfNotExist() {
        final Device givenDevice = new Device(255L, "11111222223333344444", "334445566", TRACKER);
        when(this.mockedDeviceService.findByImei(anyString())).thenReturn(Optional.of(givenDevice));

        when(this.mockedMessageService.findLastMessage(anyLong())).thenReturn(empty());

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Optional<Device> optionalActual = this.authorizationDeviceService
                .authorize(givenDevice.getImei(), givenContext);
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
                .add(this.contextArgumentCaptor.capture());
        verify(this.mockedMessageService, times(1))
                .findLastMessage(this.longArgumentCaptor.capture());
        verify(this.mockedContextManager, times(0))
                .putLastMessage(any(ChannelHandlerContext.class), any(Message.class));

        assertEquals(List.of(givenContext, givenContext, givenContext),
                this.contextArgumentCaptor.getAllValues());
        assertEquals(List.of(givenDevice.getImei(), givenDevice.getImei()),
                this.stringArgumentCaptor.getAllValues());
        assertEquals(givenDevice, this.deviceArgumentCaptor.getValue());
    }

    @Test
    public void deviceShouldNotBeAuthorized() {
        when(this.mockedDeviceService.findByImei(anyString())).thenReturn(empty());

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final String givenImei = "11111222223333344444";
        final Optional<Device> optionalDevice = this.authorizationDeviceService.authorize(givenImei, givenContext);
        assertTrue(optionalDevice.isEmpty());

        verify(this.mockedContextManager, times(1))
                .putDeviceImei(this.contextArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
        verify(this.mockedDeviceService, times(1))
                .findByImei(this.stringArgumentCaptor.capture());
        verify(this.mockedContextManager, times(0))
                .putDevice(any(ChannelHandlerContext.class), any(Device.class));
        verify(this.mockedConnectionManager, times(0))
                .add(any(ChannelHandlerContext.class));
        verify(this.mockedMessageService, times(0))
                .findLastMessage(anyLong());
        verify(this.mockedContextManager, times(0))
                .putLastMessage(any(ChannelHandlerContext.class), any(Message.class));

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
        assertEquals(List.of(givenImei, givenImei), this.stringArgumentCaptor.getAllValues());
    }
}
