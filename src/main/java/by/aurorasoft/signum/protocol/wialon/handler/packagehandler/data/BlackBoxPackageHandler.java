package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.protocol.core.contextmanager.ContextManager;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.ResponseCommandPackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.BlackBoxPackage;
import by.aurorasoft.signum.crud.service.message.MessageService;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public final class BlackBoxPackageHandler extends AbstractDataPackageHandler<BlackBoxPackage> {
    private static final String RESPONSE_TEMPLATE = "#AB#%d\r\n";

    public BlackBoxPackageHandler(ResponseCommandPackageHandler nextHandler, MessageService messageService,
                                  ContextManager contextWorker) {
        super(BlackBoxPackage.class, nextHandler, messageService, contextWorker);
    }

    @Override
    protected String createResponse(int amountSavedMessages) {
        return format(RESPONSE_TEMPLATE, amountSavedMessages);
    }
}
