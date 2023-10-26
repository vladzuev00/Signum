package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.core.exception.AnsweredException;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data.parser.exception.NotValidMessageException;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import lombok.Value;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class AbstractDataPackageDeserializerTest {
    private static final String GIVEN_PACKAGE_PREFIX = "#TEST#";
    private static final String GIVEN_FAILURE_HANDLING_RESPONSE = "#ATEST#0";

    @Mock
    private MessageParser mockedMessageParser;

    private AbstractDataPackageDeserializer deserializer;

    @Before
    public void initializeDeserializer() {
        this.deserializer = new TestDataPackageDeserializerTest(
                GIVEN_PACKAGE_PREFIX,
                this.mockedMessageParser,
                GIVEN_FAILURE_HANDLING_RESPONSE
        );
    }

    @Test
    public void messageShouldBeDeserialized() {
        final String givenMessage = "first|second|third";

        final Message firstGivenSubMessage = createMessage(1L);
        when(this.mockedMessageParser.parse(eq("first"))).thenReturn(firstGivenSubMessage);

        final Message secondGivenSubMessage = createMessage(2L);
        when(this.mockedMessageParser.parse(eq("second"))).thenReturn(secondGivenSubMessage);

        final Message thirdGivenSubMessage = createMessage(3L);
        when(this.mockedMessageParser.parse(eq("third"))).thenReturn(thirdGivenSubMessage);

        final List<Message> givenSubMessages = List.of(
                firstGivenSubMessage,
                secondGivenSubMessage,
                thirdGivenSubMessage
        );

        final Package actual = this.deserializer.deserializeMessage(givenMessage);
        final Package expected = new TestPackage(givenSubMessages);
        assertEquals(expected, actual);
    }

    @Test
    public void messageShouldNotBeDeserializedBecauseOfNotValidMessageException() {
        final String givenMessage = "first|second|third";

        final Message firstGivenSubMessage = createMessage(1L);
        when(this.mockedMessageParser.parse(eq("first"))).thenReturn(firstGivenSubMessage);

        final Message secondGivenSubMessage = createMessage(2L);
        when(this.mockedMessageParser.parse(eq("second"))).thenReturn(secondGivenSubMessage);

        when(this.mockedMessageParser.parse(eq("third"))).thenThrow(NotValidMessageException.class);

        boolean exceptionWasArisen;
        try {
            this.deserializer.deserializeMessage(givenMessage);
            exceptionWasArisen = false;
        } catch (final AnsweredException exception) {
            assertEquals(GIVEN_FAILURE_HANDLING_RESPONSE, exception.getAnswer());
            assertNotNull(exception.getCause());
            exceptionWasArisen = true;
        }
        assertTrue(exceptionWasArisen);
    }

    private static Message createMessage(final Long id) {
        return Message.builder()
                .id(id)
                .build();
    }

    @Value
    private static class TestPackage implements Package {
        List<Message> messages;
    }

    private static final class TestDataPackageDeserializerTest extends AbstractDataPackageDeserializer {
        private static final String REGEX_MESSAGES_DELIMITER = "\\|";

        public TestDataPackageDeserializerTest(final String packagePrefix,
                                               final MessageParser messageParser,
                                               final String failureHandlingResponse) {
            super(packagePrefix, messageParser, failureHandlingResponse);
        }

        @Override
        protected Stream<String> splitIntoSubMessages(final String message) {
            final String[] subMessages = message.split(REGEX_MESSAGES_DELIMITER);
            return stream(subMessages);
        }

        @Override
        protected TestPackage createPackageBySubMessages(final List<Message> messages) {
            return new TestPackage(messages);
        }
    }
}
