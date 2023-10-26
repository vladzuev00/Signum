package by.aurorasoft.signum.protocol.wialon.decoder.deserializer;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import lombok.Getter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public final class PackageDeserializerTest {

    @Test
    public void sourceShouldBeDeserialized() {
        final String givenPackagePrefix = "#TEST#";
        final Package givenDeserializedPackage = mock(Package.class);
        final TestPackageDeserializer givenDeserializer = new TestPackageDeserializer(
                givenPackagePrefix,
                givenDeserializedPackage
        );

        final String givenSource = "#TEST#content";

        final Package actual = givenDeserializer.deserialize(givenSource);
        assertSame(givenDeserializedPackage, actual);

        final String actualMessage = "content";
        final String expectedMessage = givenDeserializer.getCapturedMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    private static final class TestPackageDeserializer extends PackageDeserializer {
        private final Package deserializedPackage;

        @Getter
        private String capturedMessage;

        public TestPackageDeserializer(final String packagePrefix, final Package deserializedPackage) {
            super(packagePrefix);
            this.deserializedPackage = deserializedPackage;
        }

        @Override
        protected Package deserializeMessage(final String message) {
            this.capturedMessage = message;
            return this.deserializedPackage;
        }
    }
}
