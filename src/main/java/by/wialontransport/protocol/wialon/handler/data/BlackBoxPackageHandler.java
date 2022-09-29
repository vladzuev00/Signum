package by.wialontransport.protocol.wialon.handler.data;

import by.wialontransport.protocol.wialon.handler.FinisherPackageHandler;
import by.wialontransport.protocol.wialon.model.BlackBoxPackage;
import by.wialontransport.service.MessageService;
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
