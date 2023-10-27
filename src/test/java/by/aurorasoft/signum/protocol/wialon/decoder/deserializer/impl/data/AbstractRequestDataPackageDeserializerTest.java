package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.core.exception.AnsweredException;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data.parser.exception.NotValidMessageException;
import by.aurorasoft.signum.protocol.wialon.model.AbstractRequestDataPackage;
import by.aurorasoft.signum.protocol.wialon.model.AbstractResponseDataPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
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
public final class AbstractRequestDataPackageDeserializerTest {
    private static final String GIVEN_PACKAGE_PREFIX = "#TEST#";

    @Mock
    private MessageParser mockedMessageParser;

    private AbstractRequestDataPackageDeserializer deserializer;

    @Before
    public void initializeDeserializer() {
        this.deserializer = new TestRequestDataPackageDeserializerTest(GIVEN_PACKAGE_PREFIX, this.mockedMessageParser);
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

        final AbstractRequestDataPackage actual = this.deserializer.deserializeMessage(givenMessage);
        final AbstractRequestDataPackage expected = new TestRequestDataPackage(givenSubMessages);
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
            assertEquals(0, findCountFixedMessages(exception));
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

    private static int findCountFixedMessages(final AnsweredException exception) {
        final Package answer = exception.getAnswer();
        final AbstractResponseDataPackage responseDataPackage = (AbstractResponseDataPackage) answer;
        return responseDataPackage.getCountFixedMessages();
    }

    private static class TestRequestDataPackage extends AbstractRequestDataPackage {

        public TestRequestDataPackage(final List<Message> messages) {
            super(messages);
        }
    }

    private static final class TestResponseDataPackage extends AbstractResponseDataPackage {

        public TestResponseDataPackage(final int countFixedMessages) {
            super(countFixedMessages);
        }

    }

    private static final class TestRequestDataPackageDeserializerTest extends AbstractRequestDataPackageDeserializer {
        private static final String REGEX_MESSAGES_DELIMITER = "\\|";

        public TestRequestDataPackageDeserializerTest(final String packagePrefix, final MessageParser messageParser) {
            super(packagePrefix, messageParser, TestResponseDataPackage::new);
        }

        @Override
        protected Stream<String> splitIntoSubMessages(final String message) {
            final String[] subMessages = message.split(REGEX_MESSAGES_DELIMITER);
            return stream(subMessages);
        }

        @Override
        protected TestRequestDataPackage createPackageBySubMessages(final List<Message> messages) {
            return new TestRequestDataPackage(messages);
        }
    }
}
