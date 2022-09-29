package by.aurorasoft.signum.protocol.wialon.deserializer.impl;

import by.aurorasoft.signum.ApplicationRunner;
import by.aurorasoft.signum.protocol.wialon.model.PingPackage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationRunner.class})
public final class PingPackageDeserializerTest {

    @Autowired
    private PingPackageDeserializer deserializer;

    @Test
    public void packageShouldBeDeserialized() {
        final String givenPackage = "#P#\r\n";
        final PingPackage deserializedPackage = this.deserializer.deserialize(givenPackage);
        assertNotNull(deserializedPackage);
    }
}
