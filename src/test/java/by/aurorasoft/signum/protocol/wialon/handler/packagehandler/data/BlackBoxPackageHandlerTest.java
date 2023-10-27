package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.protocol.wialon.model.RequestBlackBoxPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class BlackBoxPackageHandlerTest extends AbstractContextTest {

    @Autowired
    private AbstractDataPackageHandler<RequestBlackBoxPackage> packageHandler;

    @Test
    public void responseShouldBeCreated() {
        final int givenAmountSavedMessages = 5;
        final String actual = this.packageHandler.createResponse(givenAmountSavedMessages);
        final String expected = "#AB#5\r\n";
        assertEquals(expected, actual);
    }
}
