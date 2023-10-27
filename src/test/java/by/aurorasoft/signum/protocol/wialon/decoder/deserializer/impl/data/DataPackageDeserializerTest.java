package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data.parser.MessageParser;
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
public final class DataPackageDeserializerTest {

    @Mock
    private MessageParser mockedParser;

    private DataPackageDeserializer deserializer;

    @Before
    public void initializeDeserializer() {
        this.deserializer = new DataPackageDeserializer(this.mockedParser);
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
        final List<Message> givenMessages = singletonList(createMessage(255L));


    }

    private static Message createMessage(final Long id) {
        return Message.builder()
                .id(id)
                .build();
    }
}
