package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.data;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.protocol.wialon.model.RequestBlackBoxPackage;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public final class RequestBlackBoxPackageDeserializerTest {
    private final RequestBlackBoxPackageDeserializer deserializer = new RequestBlackBoxPackageDeserializer(null);

    @Test
    public void messageShouldBeSplittedIntoSubMessages() {
        final String givenMessages = "first|second|third";

        final Stream<String> actual = this.deserializer.splitIntoSubMessages(givenMessages);
        final List<String> actualAsList = actual.toList();
        final List<String> expectedAsList = List.of("first", "second", "third");
        assertEquals(expectedAsList, actualAsList);
    }

    @Test
    public void packageShouldBeCreatedBySubMessages() {
        final List<Message> givenMessages = List.of(
                createMessage(1L),
                createMessage(2L),
                createMessage(3L)
        );

        final RequestBlackBoxPackage actual = this.deserializer.createPackageBySubMessages(givenMessages);
        final RequestBlackBoxPackage expected = new RequestBlackBoxPackage(givenMessages);
        assertEquals(expected, actual);
    }

    private static Message createMessage(final Long id) {
        return Message.builder()
                .id(id)
                .build();
    }
}
