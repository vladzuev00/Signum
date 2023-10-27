package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.protocol.core.service.receivemessage.ReceivingMessageService;
import by.aurorasoft.signum.protocol.wialon.model.AbstractRequestDataPackage;

import by.aurorasoft.signum.protocol.wialon.model.Package;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class AbstractDataPackageHandlerTest {
    private static final String RESPONSE_TEMPLATE = "#RESPONSE#%d";

    @Mock
    private ReceivingMessageService mockedReceivingMessageService;

    private AbstractDataPackageHandler<HandledPackage> packageHandler;

    @Captor
    private ArgumentCaptor<List<Message>> messagesArgumentCaptor;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Before
    public void initializePackageHandler() {
        this.packageHandler = new HandledPackageHandler(this.mockedReceivingMessageService);
    }

    @Test
    public void handlerShouldHandlePackage() {
        final List<Message> givenMessages = List.of(createMessage(), createMessage(), createMessage());
        final HandledPackage givenPackage = new HandledPackage(givenMessages);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        this.packageHandler.doHandle(givenPackage, givenContext);

        verify(this.mockedReceivingMessageService, times(1))
                .receive(this.contextArgumentCaptor.capture(), this.messagesArgumentCaptor.capture());
        verify(givenContext, times(1)).writeAndFlush(this.stringArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
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
    private static final class HandledPackage extends AbstractRequestDataPackage {
        public HandledPackage(List<Message> messages) {
            super(messages);
        }
    }

    @SuppressWarnings("all")
    private static final class HandledPackageHandler extends AbstractDataPackageHandler<HandledPackage> {
        public HandledPackageHandler(ReceivingMessageService receivingMessageService) {
            super(HandledPackage.class, null, receivingMessageService);
        }

        @Override
        protected String createResponse(int amountSavedMessages) {
            return format(RESPONSE_TEMPLATE, amountSavedMessages);
        }
    }

    private static Message createMessage() {
        return Message.builder()
                .datetime(now())
                .coordinate(new GpsCoordinate(1, 2))
                .speed(3)
                .course(4)
                .altitude(6)
                .amountSatellite(6)
                .parameterNamesByValues(emptyMap())
                .build();
    }
}
