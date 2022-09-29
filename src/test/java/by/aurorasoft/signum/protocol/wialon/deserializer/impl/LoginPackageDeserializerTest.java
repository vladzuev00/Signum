package by.aurorasoft.signum.protocol.wialon.deserializer.impl;

import by.aurorasoft.signum.ApplicationRunner;
import by.aurorasoft.signum.protocol.wialon.model.LoginPackage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationRunner.class})
public final class LoginPackageDeserializerTest {
    @Autowired
    private LoginPackageDeserializer deserializer;

    @Test
    public void packageShouldBeDeserialized() {
        final String givenPackage = "#L#imei;password\r\n";
        final LoginPackage actual = this.deserializer.deserialize(givenPackage);
        final LoginPackage expected = new LoginPackage("imei", "password");
        assertEquals(expected, actual);
    }
}
