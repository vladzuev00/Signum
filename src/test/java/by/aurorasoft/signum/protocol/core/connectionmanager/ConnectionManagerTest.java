package by.aurorasoft.signum.protocol.core.connectionmanager;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.model.dto.User;
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

import static by.aurorasoft.signum.crud.model.entity.DeviceEntity.Type.TRACKER;
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
//        final Device givenDevice = new Device(255L, "11111222223333344444", "223334455", TRACKER);
//        final Unit givenUnit = new Unit(256L, "unit-name", new User(257L, "user-name"), givenDevice);
//        when(this.mockedContextManager.findUnit(any(ChannelHandlerContext.class))).thenReturn(givenUnit);
//
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//        this.connectionManager.addContext(givenContext);
//
//        final Map<Long, ChannelHandlerContext> actual = findDeviceIdToContextMap(
//                this.connectionManager);
//        final Map<Long, ChannelHandlerContext> expected = Map.of(givenDevice.getId(), givenContext);
//        assertEquals(expected, actual);
//
//        verify(this.mockedContextManager, times(1))
//                .findUnit(this.contextArgumentCaptor.capture());
//
//        assertSame(givenContext, this.contextArgumentCaptor.getValue());
    }

    @Test
    public void oldContextShouldBeReplacedByNew()
            throws Exception {
//        final Device givenDevice = new Device(255L, "11111222223333344444", "223334455", TRACKER);
//        final Unit givenUnit = new Unit(256L, "unit-name", new User(257L, "user-name"), givenDevice);
//
//        when(this.mockedContextManager.findUnit(any(ChannelHandlerContext.class))).thenReturn(givenUnit);
//
//        final ChannelHandlerContext givenFirstContext = mock(ChannelHandlerContext.class);
//        this.connectionManager.addContext(givenFirstContext);
//
//        final ChannelHandlerContext givenSecondContext = mock(ChannelHandlerContext.class);
//        this.connectionManager.addContext(givenSecondContext);
//
//        final Map<Long, ChannelHandlerContext> actual = findDeviceIdToContextMap(
//                this.connectionManager);
//        final Map<Long, ChannelHandlerContext> expected = Map.of(givenDevice.getId(), givenSecondContext);
//        assertEquals(expected, actual);
//
//        verify(givenFirstContext, times(1)).close();
//        verify(this.mockedContextManager, times(2))
//                .findUnit(this.contextArgumentCaptor.capture());
//
//        final List<ChannelHandlerContext> expectedCapturedContextArguments
//                = List.of(givenFirstContext, givenSecondContext);
//        assertEquals(expectedCapturedContextArguments, this.contextArgumentCaptor.getAllValues());
    }

    @Test
    public void contextShouldBeFound()
            throws Exception {
        final Device givenDevice = new Device(255L, "11111222223333344444", "223334455", TRACKER);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Map<Long, ChannelHandlerContext> deviceIdToContextMap = findDeviceIdToContextMap(
                this.connectionManager);
        deviceIdToContextMap.put(givenDevice.getId(), givenContext);

        final Optional<ChannelHandlerContext> optionalContext = this.connectionManager.findContext(givenDevice);
        assertTrue(optionalContext.isPresent());
        final ChannelHandlerContext actual = optionalContext.get();
        assertSame(givenContext, actual);
    }

    @Test
    public void contextShouldNotBeFound() {
        final Device givenDevice = new Device(255L, "11111222223333344444", "223334455", TRACKER);
        final Optional<ChannelHandlerContext> optionalContext = this.connectionManager.findContext(givenDevice);
        assertTrue(optionalContext.isEmpty());
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
