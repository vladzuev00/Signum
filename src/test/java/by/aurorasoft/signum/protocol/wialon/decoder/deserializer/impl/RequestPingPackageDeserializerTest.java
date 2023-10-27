package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.model.RequestPingPackage;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public final class RequestPingPackageDeserializerTest {
    private final RequestPingPackageDeserializer deserializer = new RequestPingPackageDeserializer();

    @Test
    public void messageShouldBeDeserialized() {
        final String givenMessage = "";

        final RequestPingPackage actual = this.deserializer.deserializeMessage(givenMessage);
        assertNotNull(actual);
    }
}
