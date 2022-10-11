package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.PingPackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.PingPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public final class PingPackageDeserializerTest extends AbstractContextTest {

    @Autowired
    private PingPackageDeserializer deserializer;

    @Test
    public void packageShouldBeDeserialized() {
        final String givenPackage = "#P#\r\n";
        final PingPackage deserializedPackage = this.deserializer.deserialize(givenPackage);
        assertNotNull(deserializedPackage);
    }
}
