package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.protocol.core.service.receivemessage.ReceivingMessageService;
import by.aurorasoft.signum.protocol.wialon.model.DataPackage;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public final class DataPackageHandler extends AbstractDataPackageHandler<DataPackage> {
    private static final String RESPONSE_TEMPLATE = "#AD#%d\r\n";

    public DataPackageHandler(BlackBoxPackageHandler nextHandler, ReceivingMessageService receivingMessageService) {
        super(DataPackage.class, nextHandler, receivingMessageService);
    }

    @Override
    protected String createResponse(int amountSavedMessages) {
        return format(RESPONSE_TEMPLATE, amountSavedMessages);
    }
}
