package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.dto.MessageDto;
import by.aurorasoft.signum.dto.MessageDto.GpsCoordinate;
import by.aurorasoft.signum.protocol.wialon.handler.contextworker.ContextWorker;
import by.aurorasoft.signum.protocol.wialon.model.AbstractDataPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.service.MessageService;

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
    private ArgumentCaptor<List<MessageDto>> messagesArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    private AbstractDataPackageHandler<HandledPackage> packageHandler;

    @Before
    public void initializePackageHandler() {
        this.packageHandler = new HandledPackageHandler(this.mockedService, this.mockedContextWorker);
    }

    @Test
    public void handlerShouldHandlePackage() {
        final List<MessageDto> givenMessages = List.of(createMessage(), createMessage(), createMessage());
        final HandledPackage givenPackage = new HandledPackage(givenMessages);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final String givenImei = "11111222223333344444";
        when(this.mockedContextWorker.findTracker(any(ChannelHandlerContext.class))).thenReturn(givenImei);

        when(this.mockedService.save(anyList(), anyString())).thenReturn(givenMessages.size());

        final String actual = this.packageHandler.doHandle(givenPackage, givenContext);
        final String expected = "#RESPONSE#3\r\n";
        assertEquals(expected, actual);

        verify(this.mockedContextWorker, times(1))
                .findTracker(this.contextArgumentCaptor.capture());
        verify(this.mockedService, times(1))
                .save(this.messagesArgumentCaptor.capture(), this.stringArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
        assertEquals(givenMessages, this.messagesArgumentCaptor.getValue());
        assertEquals(givenImei, this.stringArgumentCaptor.getValue());
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
        public HandledPackage(List<MessageDto> messages) {
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

    private static MessageDto createMessage() {
        return new MessageDto(now(), new GpsCoordinate(1, 2), 3, 4, 5, 6,
                7.F, "");
    }
}
