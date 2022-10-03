package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.FinisherPackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.BlackBoxPackage;
import by.aurorasoft.signum.service.MessageService;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public final class BlackBoxPackageHandler extends AbstractDataPackageHandler<BlackBoxPackage> {
    private static final String RESPONSE_TEMPLATE = "#AB#%d\r\n";

    public BlackBoxPackageHandler(FinisherPackageHandler nextHandler, MessageService messageService) {
        super(BlackBoxPackage.class, nextHandler, messageService);
    }

    @Override
    protected String createResponse(int amountSavedMessages) {
        return format(RESPONSE_TEMPLATE, amountSavedMessages);
    }
}
