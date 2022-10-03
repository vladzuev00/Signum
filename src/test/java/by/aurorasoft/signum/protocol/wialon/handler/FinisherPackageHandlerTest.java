package by.aurorasoft.signum.protocol.wialon.handler;

import by.aurorasoft.signum.ApplicationRunner;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationRunner.class})
public final class FinisherPackageHandlerTest {

    @Autowired
    private FinisherPackageHandler packageHandler;

    @Test(expected = UnsupportedOperationException.class)
    public void handlerShouldNotHandlePackage() {
        final Package givenPackage = new Package() {
        };
        this.packageHandler.doHandle(givenPackage);
    }
}
