package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.LoginPackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class LoginPackageDeserializerTest extends AbstractContextTest {
    @Autowired
    private LoginPackageDeserializer deserializer;

    @Test
    public void packageShouldBeDeserialized() {
        final String givenPackage = "#L#imei;password";
        final LoginPackage actual = this.deserializer.deserialize(givenPackage);
        final LoginPackage expected = new LoginPackage("imei", "password");
        assertEquals(expected, actual);
    }
}
