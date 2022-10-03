package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import org.junit.Test;

public final class StarterPackageHandlerTest {
    private final PackageHandler packageHandler;

    public StarterPackageHandlerTest() {
        this.packageHandler = new StarterPackageHandler(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void handlerShouldNotHandlePackage() {
        final Package givenPackage = new Package() {
        };
        this.packageHandler.doHandle(givenPackage);
    }
}
