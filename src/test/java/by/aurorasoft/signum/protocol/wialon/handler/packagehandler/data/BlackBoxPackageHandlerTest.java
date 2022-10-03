package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.ApplicationRunner;
import by.aurorasoft.signum.protocol.wialon.model.BlackBoxPackage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationRunner.class})
public final class BlackBoxPackageHandlerTest {

    @Autowired
    private AbstractDataPackageHandler<BlackBoxPackage> packageHandler;

    @Test
    public void responseShouldBeCreated() {
        final int givenAmountSavedMessages = 5;
        final String actual = this.packageHandler.createResponse(givenAmountSavedMessages);
        final String expected = "#AB#5\r\n";
        assertEquals(expected, actual);
    }
}
