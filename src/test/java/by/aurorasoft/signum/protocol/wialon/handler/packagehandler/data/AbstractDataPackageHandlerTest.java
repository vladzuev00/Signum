package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.entity.Message;
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
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class AbstractDataPackageHandlerTest {
    private static final String RESPONSE_TEMPLATE = "#RESPONSE#%d\r\n";

    @Mock
    private MessageService mockedService;

    @Captor
    private ArgumentCaptor<List<Message>> messagesArgumentCaptor;

    private AbstractDataPackageHandler<HandledPackage> packageHandler;

    @Before
    public void initializePackageHandler() {
        this.packageHandler = new HandledPackageHandler(this.mockedService);
    }

    @Test
    public void handlerShouldHandlePackage() {
        final List<Message> givenMessages = List.of(createMessage(1), createMessage(2), createMessage(3));
        final HandledPackage givenPackage = new HandledPackage(givenMessages);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        when(this.mockedService.saveAndReturnSavedAmount(anyList())).thenReturn(givenMessages.size());

        final String actual = this.packageHandler.doHandle(givenPackage, givenContext);
        final String expected = "#RESPONSE#3\r\n";
        assertEquals(expected, actual);

        verify(this.mockedService, times(1))
                .saveAndReturnSavedAmount(this.messagesArgumentCaptor.capture());
        assertEquals(givenMessages, this.messagesArgumentCaptor.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void handlerShouldNotHandlePackageOfNotSuitableType() {
        final Package givenPackage = new Package() {
        };
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        this.packageHandler.doHandle(givenPackage, givenContext);
    }

    private static Message createMessage(long id) {
        final Message createdMessage = new Message();
        createdMessage.setId(id);
        return createdMessage;
    }

    @SuppressWarnings("all")
    private static final class HandledPackage extends AbstractDataPackage {
        public HandledPackage(List<Message> messages) {
            super(messages);
        }
    }

    @SuppressWarnings("all")
    private static final class HandledPackageHandler extends AbstractDataPackageHandler<HandledPackage> {
        public HandledPackageHandler(MessageService messageService) {
            super(HandledPackage.class, null, messageService);
        }

        @Override
        protected String createResponse(int amountSavedMessages) {
            return format(RESPONSE_TEMPLATE, amountSavedMessages);
        }
    }
}
