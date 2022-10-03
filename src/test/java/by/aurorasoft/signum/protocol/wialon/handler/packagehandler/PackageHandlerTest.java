package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.exception.NoSuitablePackageHandlerException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class PackageHandlerTest {
    private static final String HANDLED_PACKAGE = "result";
    private static final String FIELD_NAME_PACKAGE_TYPE = "packageType";
    private static final String FIELD_NAME_NEXT_HANDLER = "nextHandler";

    @Mock
    private PackageHandler mockedNextHandler;

    @Mock
    private Marker mockedMarker;

    @Captor
    private ArgumentCaptor<Package> packageArgumentCaptor;

    private PackageHandler packageHandler;

    @Before
    public void initializePackageHandler() {
        this.packageHandler = new PackageHandler(HandledPackage.class, this.mockedNextHandler) {
            private final Marker marker = PackageHandlerTest.this.mockedMarker;

            @Override
            protected String doHandle(Package requestPackage) {
                this.marker.mark();
                return HANDLED_PACKAGE;
            }
        };
    }

    @Test
    public void handlerShouldHandlePackageIndependently() {
        final HandledPackage givenPackage = new HandledPackage();

        final String actual = this.packageHandler.handle(givenPackage);
        assertEquals(HANDLED_PACKAGE, actual);

        verify(this.mockedMarker, times(1)).mark();
        verify(this.mockedNextHandler, times(0)).handle(any(Package.class));
    }

    @Test
    public void handlerShouldDelegateHandlingPackageToNextHandlerBecauseOfNotSuitableType() {
        final Package givenPackage = new Package() {
        };

        when(this.mockedNextHandler.handle(any(Package.class))).thenReturn(HANDLED_PACKAGE);

        final String actual = this.packageHandler.handle(givenPackage);
        assertEquals(HANDLED_PACKAGE, actual);

        verify(this.mockedMarker, times(0)).mark();
        verify(this.mockedNextHandler, times(1)).handle(this.packageArgumentCaptor.capture());
        assertSame(givenPackage, this.packageArgumentCaptor.getValue());
    }

    @Test
    public void handlerShouldDelegateHandlingPackageToNextHandlerBecauseOfHandledPackageTypeIsNull()
            throws Exception {
        setNullInField(this.packageHandler, FIELD_NAME_PACKAGE_TYPE);

        final HandledPackage givenPackage = new HandledPackage();

        when(this.mockedNextHandler.handle(any(Package.class))).thenReturn(HANDLED_PACKAGE);

        final String actual = this.packageHandler.handle(givenPackage);
        assertEquals(HANDLED_PACKAGE, actual);

        verify(this.mockedMarker, times(0)).mark();
        verify(this.mockedNextHandler, times(1)).handle(this.packageArgumentCaptor.capture());
        assertSame(givenPackage, this.packageArgumentCaptor.getValue());
    }

    @Test(expected = NoSuitablePackageHandlerException.class)
    public void packageShouldNotBeHandledBecauseOfNotSuitableTypeAndNextHandlerIsNull()
            throws Exception {
        setNullInField(this.packageHandler, FIELD_NAME_NEXT_HANDLER);

        final Package givenPackage = new Package() {
        };

        this.packageHandler.handle(givenPackage);
    }

    @SuppressWarnings("all")
    private static final class HandledPackage implements Package {

    }

    @SuppressWarnings("all")
    private static final class Marker {
        public void mark() {

        }
    }

    private static void setNullInField(PackageHandler packageHandler, String fieldName)
            throws Exception {
        final Field field = PackageHandler.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        try {
            field.set(packageHandler, null);
        } finally {
            field.setAccessible(false);
        }
    }
}
