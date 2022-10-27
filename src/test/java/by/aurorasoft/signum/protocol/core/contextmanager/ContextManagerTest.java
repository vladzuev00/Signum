package by.aurorasoft.signum.protocol.core.contextmanager;

import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.service.CommandService;
import by.aurorasoft.signum.protocol.wialon.service.sendcommand.CommandSenderService;
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
    private ArgumentCaptor<Unit> unitArgumentCaptor;

    @Before
    public void initializeContextManager() {
        this.contextManager = new ContextManager(this.mockedCommandService, this.mockedCommandSenderService,
                this.waitingResponseTimeoutInSeconds);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void unitShouldBeFoundFromContext() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<Unit> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final Unit givenUnit = mock(Unit.class);
        when(givenAttribute.get()).thenReturn(givenUnit);

        final Unit actual = this.contextManager.findUnit(givenContext);
        assertSame(givenUnit, actual);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void unitShouldBePutIntoContext() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Channel givenChannel = mock(Channel.class);
        when(givenContext.channel()).thenReturn(givenChannel);

        final Attribute<Unit> givenAttribute = mock(Attribute.class);
        when(givenChannel.attr(any(AttributeKey.class))).thenReturn(givenAttribute);

        final Unit givenUnit = mock(Unit.class);
        this.contextManager.putUnit(givenContext, givenUnit);

        verify(givenAttribute, times(1)).set(this.unitArgumentCaptor.capture());
        assertSame(givenUnit, this.unitArgumentCaptor.getValue());
    }
}
