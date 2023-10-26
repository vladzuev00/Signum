package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class LoginPackageDeserializerTest {
    private final LoginPackageDeserializer deserializer = new LoginPackageDeserializer();

    @Test
    public void messageShouldBeDeserialized() {
        final String givenMessage = "imei;password";

        final LoginPackage actual = this.deserializer.deserializeMessage(givenMessage);
        final LoginPackage expected = new LoginPackage("imei", "password");
        assertEquals(expected, actual);
    }
}
