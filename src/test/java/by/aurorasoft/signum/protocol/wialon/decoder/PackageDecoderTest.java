package by.aurorasoft.signum.protocol.wialon.decoder;

import by.aurorasoft.signum.protocol.wialon.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class PackageDecoderTest {
    private static final String PACKAGE_PREFIX = "#PREFIX#";
    private static final String FIELD_NAME_PACKAGE_PREFIX = "packagePrefix";
    private static final String FIELD_NAME_NEXT_DECODER = "nextDecoder";

    @Mock
    private PackageDecoder mockedNextDecoder;

    @Mock
    private PackageDeserializer mockedDeserializer;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    private PackageDecoder decoder;

    @Before
    public void initializeDecoder() {
        this.decoder = new PackageDecoder(this.mockedNextDecoder, PACKAGE_PREFIX, this.mockedDeserializer) {
        };
    }

    @Test
    public void packageShouldBeDecodedByHandler() {
        final String givenPackage = "#PREFIX#date\r\n";
        final Package givenResultPackage = new Package() {
        };

        when(this.mockedDeserializer.deserialize(anyString())).thenReturn(givenResultPackage);

        final Package actual = this.decoder.decode(givenPackage);
        assertSame(givenResultPackage, actual);

        verify(this.mockedDeserializer, times(1))
                .deserialize(this.stringArgumentCaptor.capture());
        assertEquals(givenPackage, this.stringArgumentCaptor.getValue());

        verify(this.mockedNextDecoder, times(0)).decode(anyString());
    }

    @Test
    public void packageShouldBeDecodedByNextHandlerBecauseOfNotSuitablePrefix() {
        final String givenPackage = "#BADPREFIX#date\r\n";
        final Package givenResultPackage = new Package() {
        };

        when(this.mockedNextDecoder.decode(anyString())).thenReturn(givenResultPackage);

        final Package actual = this.decoder.decode(givenPackage);
        assertSame(givenResultPackage, actual);

        verify(this.mockedDeserializer, times(0)).deserialize(anyString());
        verify(this.mockedNextDecoder, times(1)).decode(this.stringArgumentCaptor.capture());
        assertEquals(givenPackage, this.stringArgumentCaptor.getValue());
    }

    @Test
    public void packageShouldBeDecodedByNextHandlerBecauseOfPrefixIsNull()
            throws Exception {
        setNullAsField(this.decoder, FIELD_NAME_PACKAGE_PREFIX);

        final String givenPackage = "#OTHERPREFIX#date\r\n";
        final Package givenResultPackage = new Package() {
        };

        when(this.mockedNextDecoder.decode(anyString())).thenReturn(givenResultPackage);

        final Package actual = this.decoder.decode(givenPackage);
        assertSame(givenResultPackage, actual);

        verify(this.mockedDeserializer, times(0)).deserialize(anyString());
        verify(this.mockedNextDecoder, times(1)).decode(this.stringArgumentCaptor.capture());
        assertEquals(givenPackage, this.stringArgumentCaptor.getValue());
    }

    @Test(expected = NoSuitablePackageDecoderException.class)
    public void packageShouldNotBeDecodedBecauseOfNoSuitableDecoder()
            throws Exception {
        setNullAsField(this.decoder, FIELD_NAME_NEXT_DECODER);

        final String givenPackage = "#OTHERPREFIX#date\r\n";

        this.decoder.decode(givenPackage);
    }

    private static void setNullAsField(PackageDecoder decoder, String fieldName)
            throws Exception {
        final Field packagePrefixField = PackageDecoder.class.getDeclaredField(fieldName);
        packagePrefixField.setAccessible(true);
        try {
            packagePrefixField.set(decoder, null);
        } finally {
            packagePrefixField.setAccessible(false);
        }
    }
}
