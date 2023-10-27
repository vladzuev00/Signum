package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.protocol.wialon.model.RequestDataPackage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class DataPackageHandlerTest extends AbstractContextTest {

    @Autowired
    private AbstractDataPackageHandler<RequestDataPackage> packageHandler;

    @Test
    public void responseShouldBeCreated() {
        final int givenAmountSavedMessages = 1;
        final String actual = this.packageHandler.createResponse(givenAmountSavedMessages);
        final String expected = "#AD#1\r\n";
        assertEquals(expected, actual);
    }
}
