package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.protocol.wialon.handler.contextworker.ContextWorker;
import by.aurorasoft.signum.protocol.wialon.model.AbstractDataPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.crud.service.MessageService;

import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.lang.String.format;
import static java.time.Instant.now;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class AbstractDataPackageHandlerTest {
    private static final String RESPONSE_TEMPLATE = "#RESPONSE#%d\r\n";

    @Mock
    private MessageService mockedService;

    @Mock
    private ContextWorker mockedContextWorker;

    @Captor
    private ArgumentCaptor<List<Message>> messagesArgumentCaptor;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    private AbstractDataPackageHandler<HandledPackage> packageHandler;

    @Before
    public void initializePackageHandler() {
        this.packageHandler = new HandledPackageHandler(this.mockedService, this.mockedContextWorker);
    }

    @Test
    @SuppressWarnings("all")
    public void handlerShouldHandlePackage() {
        final List<Message> givenMessages = List.of(createMessage(), createMessage(), createMessage());
        final HandledPackage givenPackage = new HandledPackage(givenMessages);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Unit givenUnit = mock(Unit.class);
        final Long givenUnitId = 255L;
        when(givenUnit.getId()).thenReturn(givenUnitId);
        when(this.mockedContextWorker.findUnit(any(ChannelHandlerContext.class))).thenReturn(givenUnit);

        final List<Message> givenSavedMessages = mock(List.class);
        final int givenAmountSavedMessages = givenMessages.size();
        when(givenSavedMessages.size()).thenReturn(givenAmountSavedMessages);
        when(this.mockedService.saveAll(anyLong(), anyList())).thenReturn(givenSavedMessages);

        final String actual = this.packageHandler.doHandle(givenPackage, givenContext);
        final String expected = "#RESPONSE#3\r\n";
        assertEquals(expected, actual);

        verify(this.mockedContextWorker, times(1))
                .findUnit(this.contextArgumentCaptor.capture());
        verify(this.mockedService, times(1))
                .saveAll(this.longArgumentCaptor.capture(), this.messagesArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
        assertEquals(givenUnitId, this.longArgumentCaptor.getValue());
        assertSame(givenMessages, this.messagesArgumentCaptor.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void handlerShouldNotHandlePackageOfNotSuitableType() {
        final Package givenPackage = new Package() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.packageHandler.doHandle(givenPackage, givenContext);
    }

    @SuppressWarnings("all")
    private static final class HandledPackage extends AbstractDataPackage {
        public HandledPackage(List<Message> messages) {
            super(messages);
        }
    }

    @SuppressWarnings("all")
    private static final class HandledPackageHandler extends AbstractDataPackageHandler<HandledPackage> {
        public HandledPackageHandler(MessageService messageService, ContextWorker contextWorker) {
            super(HandledPackage.class, null, messageService, contextWorker);
        }

        @Override
        protected String createResponse(int amountSavedMessages) {
            return format(RESPONSE_TEMPLATE, amountSavedMessages);
        }
    }

    private static Message createMessage() {
        return new Message(now(), new GpsCoordinate(1, 2), 3, 4, 5, 6,
                7.F, "");
    }
}
