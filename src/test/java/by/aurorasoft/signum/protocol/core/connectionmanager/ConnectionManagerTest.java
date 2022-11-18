package by.aurorasoft.signum.protocol.core.connectionmanager;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static by.aurorasoft.signum.crud.model.dto.Device.Type.TRACKER;
import static java.lang.Long.MIN_VALUE;
import static java.util.Map.copyOf;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ConnectionManagerTest {
    private static final String FIELD_NAME_DEVICE_ID_TO_CONTEXT_MAP = "deviceIdToContextMap";

    @Mock
    private ContextManager mockedContextManager;

    private ConnectionManager connectionManager;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Before
    public void initializeConnectionManager() {
        this.connectionManager = new ConnectionManager(this.mockedContextManager);
    }

    @Test
    public void contextShouldBeAdded()
            throws Exception {
        final Device givenDevice = new Device(255L, "11111222223333344444", "223334455", TRACKER);
        when(this.mockedContextManager.findDevice(any(ChannelHandlerContext.class))).thenReturn(givenDevice);

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.connectionManager.add(givenContext);

        final Map<Long, ChannelHandlerContext> actual = findDeviceIdToContextMap(this.connectionManager);
        final Map<Long, ChannelHandlerContext> expected = Map.of(givenDevice.getId(), givenContext);
        assertEquals(expected, actual);

        verify(this.mockedContextManager, times(1))
                .findDevice(this.contextArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
    }

    @Test
    public void oldContextShouldBeReplacedByNew()
            throws Exception {
        final Device givenDevice = new Device(255L, "11111222223333344444", "223334455", TRACKER);

        when(this.mockedContextManager.findDevice(any(ChannelHandlerContext.class))).thenReturn(givenDevice);

        final ChannelHandlerContext givenFirstContext = mock(ChannelHandlerContext.class);
        this.connectionManager.add(givenFirstContext);

        final ChannelHandlerContext givenSecondContext = mock(ChannelHandlerContext.class);
        this.connectionManager.add(givenSecondContext);

        final Map<Long, ChannelHandlerContext> actual = findDeviceIdToContextMap(
                this.connectionManager);
        final Map<Long, ChannelHandlerContext> expected = Map.of(givenDevice.getId(), givenSecondContext);
        assertEquals(expected, actual);

        verify(givenFirstContext, times(1)).close();
        verify(this.mockedContextManager, times(2))
                .findDevice(this.contextArgumentCaptor.capture());

        final List<ChannelHandlerContext> expectedCapturedContextArguments
                = List.of(givenFirstContext, givenSecondContext);
        assertEquals(expectedCapturedContextArguments, this.contextArgumentCaptor.getAllValues());
    }

    @Test
    public void contextShouldBeFoundByDeviceId()
            throws Exception {
        final Device givenDevice = new Device(255L, "11111222223333344444", "223334455", TRACKER);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Map<Long, ChannelHandlerContext> deviceIdToContextMap = findDeviceIdToContextMap(
                this.connectionManager);
        deviceIdToContextMap.put(givenDevice.getId(), givenContext);

        final Optional<ChannelHandlerContext> optionalContext = this.connectionManager
                .find(givenDevice.getId());
        assertTrue(optionalContext.isPresent());
        final ChannelHandlerContext actual = optionalContext.get();
        assertSame(givenContext, actual);
    }

    @Test
    public void contextShouldNotBeFound() {
        final Optional<ChannelHandlerContext> optionalContext = this.connectionManager
                .find(MIN_VALUE);
        assertTrue(optionalContext.isEmpty());
    }

    @Test
    public void contextShouldBeRemovedByDeviceId()
            throws Exception {
        final Long givenDeviceId = 255L;
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Map<Long, ChannelHandlerContext> deviceIdToContextMap = findDeviceIdToContextMap(
                this.connectionManager);
        deviceIdToContextMap.put(givenDeviceId, givenContext);

        this.connectionManager.remove(givenDeviceId);

        assertTrue(deviceIdToContextMap.isEmpty());
    }

    @Test
    public void contextShouldNotBeRemovedByNotExistingDeviceId()
            throws Exception {
        final Long givenFirstDeviceId = 255L;
        final ChannelHandlerContext givenFirstContext = mock(ChannelHandlerContext.class);

        final Long givenSecondDeviceId = 256L;
        final ChannelHandlerContext givenSecondContext = mock(ChannelHandlerContext.class);

        final Map<Long, ChannelHandlerContext> deviceIdToContextMap = findDeviceIdToContextMap(
                this.connectionManager);
        deviceIdToContextMap.put(givenFirstDeviceId, givenFirstContext);
        deviceIdToContextMap.put(givenSecondDeviceId, givenSecondContext);

        final Long givenDeviceIdToRemoveEntry = 257L;
        final Map<Long, ChannelHandlerContext> deviceIdToContextMapBeforeRemove = copyOf(deviceIdToContextMap);
        this.connectionManager.remove(givenDeviceIdToRemoveEntry);
        assertEquals(deviceIdToContextMapBeforeRemove, deviceIdToContextMap);
    }

    @SuppressWarnings("unchecked")
    private static Map<Long, ChannelHandlerContext> findDeviceIdToContextMap(ConnectionManager connectionManager)
            throws Exception {
        final Field deviceIdToContextMapField = ConnectionManager.class
                .getDeclaredField(FIELD_NAME_DEVICE_ID_TO_CONTEXT_MAP);
        deviceIdToContextMapField.setAccessible(true);
        try {
            return (Map<Long, ChannelHandlerContext>) deviceIdToContextMapField.get(connectionManager);
        } finally {
            deviceIdToContextMapField.setAccessible(false);
        }
    }
}
