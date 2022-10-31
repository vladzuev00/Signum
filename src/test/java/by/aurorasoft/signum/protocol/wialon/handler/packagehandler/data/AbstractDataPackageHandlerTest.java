package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
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
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class AbstractDataPackageHandlerTest {
    private static final String RESPONSE_TEMPLATE = "#RESPONSE#%d";

    @Mock
    private MessageService mockedService;

    @Mock
    private ContextManager mockedContextManager;

    private AbstractDataPackageHandler<HandledPackage> packageHandler;

    @Captor
    private ArgumentCaptor<List<Message>> messagesArgumentCaptor;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Before
    public void initializePackageHandler() {
        this.packageHandler = new HandledPackageHandler(this.mockedService, this.mockedContextManager);
    }

    @Test
    @SuppressWarnings("all")
    public void handlerShouldHandlePackage() {
        final List<Message> givenMessages = List.of(createMessage(), createMessage(), createMessage());
        final HandledPackage givenPackage = new HandledPackage(givenMessages);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Device givenDevice = mock(Device.class);
        final Long givenDeviceId = 255L;
        when(givenDevice.getId()).thenReturn(givenDeviceId);

        final Unit givenUnit = mock(Unit.class);
        //when(givenUnit.getDevice()).thenReturn(givenDevice);

//        when(this.mockedContextManager.findUnit(any(ChannelHandlerContext.class))).thenReturn(givenUnit);

        final List<Message> givenSavedMessages = mock(List.class);
        final int givenAmountSavedMessages = givenMessages.size();
        when(givenSavedMessages.size()).thenReturn(givenAmountSavedMessages);
        when(this.mockedService.saveAll(anyLong(), anyList())).thenReturn(givenSavedMessages);

        this.packageHandler.doHandle(givenPackage, givenContext);

//        verify(this.mockedContextManager, times(1))
//                .findUnit(this.contextArgumentCaptor.capture());
        verify(this.mockedService, times(1))
                .saveAll(this.longArgumentCaptor.capture(), this.messagesArgumentCaptor.capture());
        verify(givenContext, times(1))
                .writeAndFlush(this.stringArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
        assertEquals(givenDeviceId, this.longArgumentCaptor.getValue());
        assertSame(givenMessages, this.messagesArgumentCaptor.getValue());
        assertEquals("#RESPONSE#3", this.stringArgumentCaptor.getValue());
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
        public HandledPackageHandler(MessageService messageService, ContextManager contextWorker) {
            super(HandledPackage.class, null, messageService, contextWorker);
        }

        @Override
        protected String createResponse(int amountSavedMessages) {
            return format(RESPONSE_TEMPLATE, amountSavedMessages);
        }
    }

    private static Message createMessage() {
        return new Message(now(), new GpsCoordinate(1, 2), 3, 4, 5, 6,
                emptyMap());
    }
}
