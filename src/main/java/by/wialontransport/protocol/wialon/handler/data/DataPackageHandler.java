package by.wialontransport.protocol.wialon.handler.data;

import by.wialontransport.protocol.wialon.model.DataPackage;
import by.wialontransport.service.MessageService;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public final class DataPackageHandler extends AbstractDataPackageHandler<DataPackage> {
    private static final String RESPONSE_TEMPLATE = "#AD#%d\r\n";

    public DataPackageHandler(BlackBoxPackageHandler nextHandler, MessageService messageService) {
        super(DataPackage.class, nextHandler, messageService);
    }

    @Override
    protected String createResponse(int amountSavedMessages) {
        return format(RESPONSE_TEMPLATE, amountSavedMessages);
    }
}
