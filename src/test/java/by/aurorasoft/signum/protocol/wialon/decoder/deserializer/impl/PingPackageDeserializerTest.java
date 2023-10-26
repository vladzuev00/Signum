package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.model.PingPackage;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public final class PingPackageDeserializerTest {
    private final PingPackageDeserializer deserializer = new PingPackageDeserializer();

    @Test
    public void packageShouldBeDeserialized() {
        final String givenMessage = "";

        final PingPackage actual = this.deserializer.deserializeMessage(givenMessage);
        assertNotNull(actual);
    }
}
