package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import by.aurorasoft.signum.protocol.wialon.model.PingPackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class PingPackageHandlerTest {
    private final PackageHandler packageHandler;

    public PingPackageHandlerTest() {
        this.packageHandler = new PingPackageHandler(null);
    }

    @Test
    public void handlerShouldHandlePackage() {
        final Package givenPackage = new PingPackage();

        final String actual = this.packageHandler.doHandle(givenPackage);
        final String expected = "#AP#";
        assertEquals(expected, actual);
    }
}
