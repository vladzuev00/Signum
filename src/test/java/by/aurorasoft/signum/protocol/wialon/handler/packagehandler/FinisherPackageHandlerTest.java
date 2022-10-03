package by.aurorasoft.signum.protocol.wialon.handler.packagehandler;

import by.aurorasoft.signum.protocol.wialon.model.Package;
import org.junit.Test;

public final class FinisherPackageHandlerTest {

    private final FinisherPackageHandler packageHandler;

    public FinisherPackageHandlerTest() {
        this.packageHandler = new FinisherPackageHandler();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void handlerShouldNotHandlePackage() {
        final Package givenPackage = new Package() {
        };
        this.packageHandler.doHandle(givenPackage);
    }
}
