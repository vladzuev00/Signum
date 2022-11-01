package by.aurorasoft.signum.protocol.core.contextmanager;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.service.CommandService;
import by.aurorasoft.signum.protocol.core.service.CommandSenderService;
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
import org.springframework.beans.factory.annotation.Value;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ContextManagerTest {

    @Mock
    private CommandService mockedCommandService;

    @Mock
    private CommandSenderService mockedCommandSenderService;

    @Value("${netty.contextManager.lifecycleObserver.waitingResponseTimeoutInSeconds}")
    private int waitingResponseTimeoutInSeconds;

    private ContextManager contextManager;

    @Captor
    private ArgumentCaptor<Device> deviceArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Before
    public void initializeContextManager() {
        this.contextManager = new ContextManager(this.mockedCommandService, this.mockedCommandSenderService,
                this.waitingResponseTimeoutInSeconds);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void deviceImeiShouldBeFound() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<String> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final String givenImei = "11112222333344445555";
        when(givenAttribute.get()).thenReturn(givenImei);

        final String actual = this.contextManager.findDeviceImei(givenContext);
        assertSame(givenImei, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void deviceImeiShouldBePut() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<String> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final String givenImei = "11112222333344445555";
        this.contextManager.putDeviceImei(givenContext, givenImei);

        verify(givenAttribute, times(1))
                .set(this.stringArgumentCaptor.capture());
        assertSame(givenImei, this.stringArgumentCaptor.getValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void deviceShouldBeFoundFromContext() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<Device> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final Device givenDevice = mock(Device.class);
        when(givenAttribute.get()).thenReturn(givenDevice);

        final Device actual = this.contextManager.findDevice(givenContext);
        assertSame(givenDevice, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void unitShouldBePutIntoContext() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<Device> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final Device givenDevice = mock(Device.class);
        this.contextManager.putDevice(givenContext, givenDevice);

        verify(givenAttribute, times(1))
                .set(this.deviceArgumentCaptor.capture());
        assertSame(givenDevice, this.deviceArgumentCaptor.getValue());
    }
}
