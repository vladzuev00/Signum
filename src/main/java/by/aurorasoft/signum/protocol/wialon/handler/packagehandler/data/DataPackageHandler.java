package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.model.DataPackage;
import by.aurorasoft.signum.crud.service.MessageService;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public final class DataPackageHandler extends AbstractDataPackageHandler<DataPackage> {
    private static final String RESPONSE_TEMPLATE = "#AD#%d\r\n";

    public DataPackageHandler(BlackBoxPackageHandler nextHandler, MessageService messageService,
                              ContextManager contextWorker) {
        super(DataPackage.class, nextHandler, messageService, contextWorker);
    }

    @Override
    protected String createResponse(int amountSavedMessages) {
        return format(RESPONSE_TEMPLATE, amountSavedMessages);
    }
}
