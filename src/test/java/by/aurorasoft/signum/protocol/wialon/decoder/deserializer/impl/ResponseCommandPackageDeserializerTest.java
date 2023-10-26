package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl;

import by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage;
import by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage.Status;
import org.junit.Test;
import org.mockito.MockedStatic;

import static by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage.Status.SUCCESS;
import static by.aurorasoft.signum.protocol.wialon.model.ResponseCommandPackage.Status.findByValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

public final class ResponseCommandPackageDeserializerTest {
    private final ResponseCommandPackageDeserializer deserializer = new ResponseCommandPackageDeserializer();

    @Test
    public void messageShouldBeDeserialized() {
        try (final MockedStatic<Status> mockedStaticStatus = mockStatic(Status.class)) {
            final String givenMessage = "1";

            final byte expectedStatusValue = 1;
            final Status givenStatus = SUCCESS;
            mockedStaticStatus.when(() -> findByValue(eq(expectedStatusValue))).thenReturn(givenStatus);

            final ResponseCommandPackage actual = this.deserializer.deserializeMessage(givenMessage);
            final ResponseCommandPackage expected = new ResponseCommandPackage(givenStatus);
            assertEquals(expected, actual);
        }
    }
}
