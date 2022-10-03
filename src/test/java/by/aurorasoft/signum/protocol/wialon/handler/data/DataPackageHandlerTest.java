package by.aurorasoft.signum.protocol.wialon.handler.data;

import by.aurorasoft.signum.ApplicationRunner;
import by.aurorasoft.signum.protocol.wialon.model.DataPackage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationRunner.class})
public final class DataPackageHandlerTest {

    @Autowired
    private AbstractDataPackageHandler<DataPackage> packageHandler;

    @Test
    public void responseShouldBeCreated() {
        final int givenAmountSavedMessages = 1;
        final String actual = this.packageHandler.createResponse(givenAmountSavedMessages);
        final String expected = "#AD#1\r\n";
        assertEquals(expected, actual);
    }
}
