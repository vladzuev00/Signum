package by.aurorasoft.signum.protocol.wialon.handler.packagehandler.data;

import by.aurorasoft.signum.protocol.core.service.receivemessage.ReceivingMessageService;
import by.aurorasoft.signum.protocol.wialon.handler.packagehandler.ResponseCommandPackageHandler;
import by.aurorasoft.signum.protocol.wialon.model.RequestBlackBoxPackage;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public final class BlackBoxPackageHandler extends AbstractDataPackageHandler<RequestBlackBoxPackage> {
    private static final String RESPONSE_TEMPLATE = "#AB#%d\r\n";

    public BlackBoxPackageHandler(ResponseCommandPackageHandler nextHandler,
                                  ReceivingMessageService receivingMessageService) {
        super(RequestBlackBoxPackage.class, nextHandler, receivingMessageService);
    }

    @Override
    protected String createResponse(int amountSavedMessages) {
        return format(RESPONSE_TEMPLATE, amountSavedMessages);
    }
}
