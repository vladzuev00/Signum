package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.model.RequestLoginPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class LoginPackageDeserializerTest {
    private final LoginPackageDeserializer deserializer = new LoginPackageDeserializer();

    @Test
    public void messageShouldBeDeserialized() {
        final String givenMessage = "imei;password";

        final RequestLoginPackage actual = this.deserializer.deserializeMessage(givenMessage);
        final RequestLoginPackage expected = new RequestLoginPackage("imei", "password");
        assertEquals(expected, actual);
    }
}
