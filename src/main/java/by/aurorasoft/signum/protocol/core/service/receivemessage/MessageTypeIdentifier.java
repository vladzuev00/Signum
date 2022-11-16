package by.aurorasoft.signum.protocol.core.service.receivemessage;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.*;

@Component
@RequiredArgsConstructor
public final class MessageTypeIdentifier {
    private final MessagePropertyValidator propertyValidator;

    public MessageType identify(Message current) {
        if (this.isValid(current)) {
            return VALID;
        } else if (this.isCorrect(current)) {
            return CORRECT;
        } else {
            return INCORRECT;
        }
    }

    public MessageType identify(Message current, Message previous) {
        if (this.isValid(current, previous)) {
            return VALID;
        } else if (this.isCorrect(current, previous)) {
            return CORRECT;
        } else if (this.isWrongOrder(current, previous)) {
            return WRONG_ORDER;
        } else {
            return INCORRECT;
        }
    }

    private boolean isValid(Message research) {
        return this.propertyValidator.isValidDateTime(research)
                && this.propertyValidator.isValidAmountSatellite(research)
                && this.propertyValidator.areValidCoordinateParameters(research);
    }

    private boolean isValid(Message research, Message previous) {
        return this.isValid(research) && isValidOrder(research, previous);
    }

    private static boolean isValidOrder(Message research, Message previous) {
        return research.getDatetime().isAfter(previous.getDatetime());
    }

    private boolean isCorrect(Message research) {
        return this.propertyValidator.isValidDateTime(research)
                && !(this.propertyValidator.isValidAmountSatellite(research)
                && this.propertyValidator.areValidCoordinateParameters(research));
    }

    private boolean isCorrect(Message research, Message previous) {
        return this.isCorrect(research) && isValidOrder(research, previous);
    }

    private boolean isWrongOrder(Message research, Message previous) {
        return this.propertyValidator.isValidDateTime(research) && !isValidOrder(research, previous);
    }
}
