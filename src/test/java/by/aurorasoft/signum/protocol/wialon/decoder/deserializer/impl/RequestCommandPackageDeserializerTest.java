package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.model.RequestCommandPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class RequestCommandPackageDeserializerTest {
    private final RequestCommandPackageDeserializer deserializer = new RequestCommandPackageDeserializer();

    @Test
    public void messageShouldBeDeserialized() {
        final String givenMessage = "message";

        final RequestCommandPackage actual = this.deserializer.deserializeMessage(givenMessage);
        final RequestCommandPackage expected = new RequestCommandPackage(givenMessage);
        assertEquals(expected, actual);
    }
}
