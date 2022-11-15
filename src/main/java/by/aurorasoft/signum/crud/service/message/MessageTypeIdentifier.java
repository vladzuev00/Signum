package by.aurorasoft.signum.crud.service.message;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.*;

@Component
@RequiredArgsConstructor
final class MessageTypeIdentifier {
    private final MessagePropertyValidator propertyValidator;

    public MessageType identify(Message message) {
        if (this.isValid(message)) {
            return VALID;
        } else if (this.isCorrect(message)) {
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
                && this.propertyValidator.areValidCoordinates(research);
    }

    private boolean isValid(Message research, Message previous) {
        return this.isValid(research) && isValidOrder(research, previous);
    }

    private boolean isCorrect(Message research) {
        return this.propertyValidator.isValidDateTime(research)
                && !(this.propertyValidator.isValidAmountSatellite(research)
                && this.propertyValidator.areValidCoordinates(research));
    }

    private boolean isCorrect(Message research, Message previous) {
        return this.isCorrect(research) && isValidOrder(research, previous);
    }

    private boolean isWrongOrder(Message research, Message last) {
        return this.propertyValidator.isValidDateTime(research) && !isValidOrder(research, last);
    }

    private static boolean isValidOrder(Message current, Message previous) {
        return current.getDatetime().isAfter(previous.getDatetime());
    }
}
