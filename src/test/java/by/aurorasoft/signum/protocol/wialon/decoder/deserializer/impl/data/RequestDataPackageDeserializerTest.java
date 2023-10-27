package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.model.RequestDataPackage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public final class RequestDataPackageDeserializerTest {

    @Mock
    private MessageParser mockedParser;

    private RequestDataPackageDeserializer deserializer;

    @Before
    public void initializeDeserializer() {
        this.deserializer = new RequestDataPackageDeserializer(this.mockedParser);
    }

    @Test
    public void messageShouldBeSplittedToSubMessages() {
        final String givenMessage = "message";

        final Stream<String> actual = this.deserializer.splitIntoSubMessages(givenMessage);
        final List<String> actualAsList = actual.toList();
        final List<String> expectedAsList = List.of(givenMessage);
        assertEquals(expectedAsList, actualAsList);
    }

    @Test
    public void packageShouldBeCreatedBySubMessages() {
        final Message givenMessage = createMessage(255L);
        final List<Message> givenMessages = singletonList(givenMessage);

        final RequestDataPackage actual = this.deserializer.createPackageBySubMessages(givenMessages);
        final RequestDataPackage expected = new RequestDataPackage(givenMessage);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void packageShouldNotBeCreatedBySubMessages() {
        final List<Message> givenMessages = List.of(
                createMessage(255L),
                createMessage(256L)
        );

        this.deserializer.createPackageBySubMessages(givenMessages);
    }

    private static Message createMessage(final Long id) {
        return Message.builder()
                .id(id)
                .build();
    }
}
