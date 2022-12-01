package by.aurorasoft.signum.protocol.core.service.receivemessage;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.*;
import static java.time.Instant.parse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class MessageTypeIdentifierTest {

    @Mock
    private MessagePropertyValidator mockedPropertyValidator;

    private MessageTypeIdentifier typeIdentifier;

    @Captor
    private ArgumentCaptor<Message> messageArgumentCaptor;

    @Before
    public void initializeTypeIdentifier() {
        this.typeIdentifier = new MessageTypeIdentifier(this.mockedPropertyValidator);
    }

    @Test
    public void currentMessageShouldBeIdentifiedAsValidWithoutPreviousMessage() {
        final Message givenMessage = Message.builder().build();

        when(this.mockedPropertyValidator.isValidDateTime(any(Message.class))).thenReturn(true);
        when(this.mockedPropertyValidator.isValidAmountSatellite(any(Message.class))).thenReturn(true);
        when(this.mockedPropertyValidator.areValidCoordinateParameters(any(Message.class))).thenReturn(true);

        final MessageType actual = this.typeIdentifier.identify(givenMessage);
        assertSame(VALID, actual);
    }

    @Test
    public void currentMessageShouldBeIdentifiedAsCorrectWithoutPreviousMessageBecauseOfNotValidAmountSatellite() {
        final Message givenMessage = Message.builder().build();

        when(this.mockedPropertyValidator.isValidDateTime(any(Message.class))).thenReturn(true);
        when(this.mockedPropertyValidator.isValidAmountSatellite(any(Message.class))).thenReturn(false);

        final MessageType actual = this.typeIdentifier.identify(givenMessage);
        assertSame(CORRECT, actual);
    }

    @Test
    public void currentMessageShouldBeIdentifiedAsCorrectWithoutPreviousMessageBecauseOfNotValidCoordinateParameters() {
        final Message givenMessage = Message.builder().build();

        when(this.mockedPropertyValidator.isValidDateTime(any(Message.class))).thenReturn(true);
        when(this.mockedPropertyValidator.isValidAmountSatellite(any(Message.class))).thenReturn(true);
        when(this.mockedPropertyValidator.areValidCoordinateParameters(any(Message.class))).thenReturn(false);

        final MessageType actual = this.typeIdentifier.identify(givenMessage);
        assertSame(CORRECT, actual);
    }

    @Test
    public void currentMessageShouldBeIdentifiedAsIncorrectWithoutPreviousMessageBecauseOfNotValidDateTime() {
        final Message givenMessage = Message.builder().build();

        when(this.mockedPropertyValidator.isValidDateTime(any(Message.class))).thenReturn(false);

        final MessageType actual = this.typeIdentifier.identify(givenMessage);
        assertSame(INCORRECT, actual);
    }

    @Test
    public void currentMessageShouldBeIdentifiedAsValidWithPreviousMessage() {
        final Message givenPreviousMessage = mock(Message.class);
        when(givenPreviousMessage.getDatetime()).thenReturn(parse("2021-01-01T00:00:01Z"));

        final Message givenCurrentMessage = mock(Message.class);
        when(givenCurrentMessage.getDatetime()).thenReturn(parse("2021-01-01T00:00:02Z"));

        when(this.mockedPropertyValidator.isValidDateTime(any(Message.class))).thenReturn(true);
        when(this.mockedPropertyValidator.isValidAmountSatellite(any(Message.class))).thenReturn(true);
        when(this.mockedPropertyValidator.areValidCoordinateParameters(any(Message.class))).thenReturn(true);

        final MessageType actual = this.typeIdentifier.identify(givenCurrentMessage, givenPreviousMessage);
        assertSame(VALID, actual);

        verify(this.mockedPropertyValidator, times(1))
                .isValidDateTime(this.messageArgumentCaptor.capture());
        verify(this.mockedPropertyValidator, times(1))
                .isValidAmountSatellite(this.messageArgumentCaptor.capture());
        verify(this.mockedPropertyValidator, times(1))
                .areValidCoordinateParameters(this.messageArgumentCaptor.capture());

        assertTrue(
                this.messageArgumentCaptor.getAllValues().stream()
                        .allMatch(capturedMessage -> capturedMessage == givenCurrentMessage)
        );
    }

    @Test
    public void currentMessageShouldBeIdentifiedAsCorrectWithPreviousMessageBecauseOfNotValidAmountSatellite() {
        final Message givenPreviousMessage = mock(Message.class);
        when(givenPreviousMessage.getDatetime()).thenReturn(parse("2021-01-01T00:00:01Z"));

        final Message givenCurrentMessage = mock(Message.class);
        when(givenCurrentMessage.getDatetime()).thenReturn(parse("2021-01-01T00:00:02Z"));

        when(this.mockedPropertyValidator.isValidDateTime(any(Message.class))).thenReturn(true);
        when(this.mockedPropertyValidator.isValidAmountSatellite(any(Message.class))).thenReturn(false);

        final MessageType actual = this.typeIdentifier.identify(givenCurrentMessage, givenPreviousMessage);
        assertSame(CORRECT, actual);

        verify(this.mockedPropertyValidator, times(2))
                .isValidDateTime(this.messageArgumentCaptor.capture());
        verify(this.mockedPropertyValidator, times(2))
                .isValidAmountSatellite(this.messageArgumentCaptor.capture());
        verify(this.mockedPropertyValidator, times(0))
                .areValidCoordinateParameters(this.messageArgumentCaptor.capture());

        assertTrue(
                this.messageArgumentCaptor.getAllValues().stream()
                        .allMatch(capturedMessage -> capturedMessage == givenCurrentMessage)
        );
    }

    @Test
    public void currentMessageShouldBeIdentifiedAsCorrectWithPreviousMessageBecauseOfNotValidCoordinateParameters() {
        final Message givenPreviousMessage = mock(Message.class);
        when(givenPreviousMessage.getDatetime()).thenReturn(parse("2021-01-01T00:00:02Z"));

        final Message givenCurrentMessage = mock(Message.class);
        when(givenCurrentMessage.getDatetime()).thenReturn(parse("2021-01-01T00:00:03Z"));

        when(this.mockedPropertyValidator.isValidDateTime(any(Message.class))).thenReturn(true);
        when(this.mockedPropertyValidator.isValidAmountSatellite(any(Message.class))).thenReturn(true);
        when(this.mockedPropertyValidator.areValidCoordinateParameters(any(Message.class))).thenReturn(false);

        final MessageType actual = this.typeIdentifier.identify(givenCurrentMessage, givenPreviousMessage);
        assertSame(CORRECT, actual);

        verify(this.mockedPropertyValidator, times(2))
                .isValidDateTime(this.messageArgumentCaptor.capture());
        verify(this.mockedPropertyValidator, times(2))
                .isValidAmountSatellite(this.messageArgumentCaptor.capture());
        verify(this.mockedPropertyValidator, times(2))
                .areValidCoordinateParameters(this.messageArgumentCaptor.capture());

        assertTrue(
                this.messageArgumentCaptor.getAllValues().stream()
                        .allMatch(capturedMessage -> capturedMessage == givenCurrentMessage)
        );
    }

    @Test
    public void currentMessageShouldBeIdentifiedAsWrongOrderWithPreviousMessage() {
        final Message givenPreviousMessage = mock(Message.class);
        when(givenPreviousMessage.getDatetime()).thenReturn(parse("2021-01-02T00:00:00Z"));

        final Message givenCurrentMessage = mock(Message.class);
        when(givenCurrentMessage.getDatetime()).thenReturn(parse("2021-01-01T00:00:00Z"));

        when(this.mockedPropertyValidator.isValidDateTime(any(Message.class))).thenReturn(true);
        when(this.mockedPropertyValidator.isValidAmountSatellite(any(Message.class))).thenReturn(true);
        when(this.mockedPropertyValidator.areValidCoordinateParameters(any(Message.class))).thenReturn(true);

        final MessageType actual = this.typeIdentifier.identify(givenCurrentMessage, givenPreviousMessage);
        assertSame(WRONG_ORDER, actual);

        verify(this.mockedPropertyValidator, times(3))
                .isValidDateTime(this.messageArgumentCaptor.capture());
        verify(this.mockedPropertyValidator, times(2))
                .isValidAmountSatellite(this.messageArgumentCaptor.capture());
        verify(this.mockedPropertyValidator, times(2))
                .areValidCoordinateParameters(this.messageArgumentCaptor.capture());

        assertTrue(
                this.messageArgumentCaptor.getAllValues().stream()
                        .allMatch(capturedMessage -> capturedMessage == givenCurrentMessage)
        );
    }

    @Test
    public void currentMessageShouldBeIdentifiedAsIncorrectWithPreviousMessageBecauseOfNotValidDateTime() {
        final Message givenPreviousMessage = mock(Message.class);
        final Message givenCurrentMessage = mock(Message.class);

        when(this.mockedPropertyValidator.isValidDateTime(any(Message.class))).thenReturn(false);

        final MessageType actual = this.typeIdentifier.identify(givenCurrentMessage, givenPreviousMessage);
        assertSame(INCORRECT, actual);

        verify(this.mockedPropertyValidator, times(3))
                .isValidDateTime(this.messageArgumentCaptor.capture());
        verify(this.mockedPropertyValidator, times(0))
                .isValidAmountSatellite(this.messageArgumentCaptor.capture());
        verify(this.mockedPropertyValidator, times(0))
                .areValidCoordinateParameters(this.messageArgumentCaptor.capture());

        assertTrue(
                this.messageArgumentCaptor.getAllValues().stream()
                        .allMatch(capturedMessage -> capturedMessage == givenCurrentMessage)
        );
    }
}
