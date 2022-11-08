package by.aurorasoft.signum.crud.service.message;

import by.aurorasoft.signum.config.property.MessageValidationProperty;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.ParameterName;
import by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;
import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.*;
import static java.lang.Float.compare;
import static java.time.Instant.now;
import static java.time.Instant.parse;

@Component
@RequiredArgsConstructor
public final class MessageValidator {
    private static final Instant MIN_ALLOWABLE_VALID_DATE_TIME = parse("2020-01-01T00:00:00Z");

    private final MessageValidationProperty property;

    public MessageType validate(Message current, Message previous) {
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

    public MessageType validate(Message message) {
        if (this.isValid(message)) {
            return VALID;
        } else if (this.isCorrect(message)) {
            return CORRECT;
        } else {
            return INCORRECT;
        }
    }

    private boolean isValid(Message message) {
        return isValidDateTime(message)
                && this.isValidAmountSatellite(message)
                && this.areValidCoordinates(message);
    }

    private boolean isValid(Message current, Message previous) {
        return this.isValid(current) && isValidOrder(current, previous);
    }

    private boolean isCorrect(Message message) {
        return isValidDateTime(message)
                && !(this.isValidAmountSatellite(message) && this.areValidCoordinates(message));
    }

    private boolean isCorrect(Message current, Message previous) {
        return this.isCorrect(current) && isValidOrder(current, previous);
    }

    private boolean isWrongOrder(Message research, Message last) {
        return isValidDateTime(research) && !isValidOrder(research, last);
    }

    private boolean isValidAmountSatellite(Message message) {
        return message.getAmountSatellite() >= this.property.getMinValidAmountSatellite();
    }

    private boolean areValidCoordinates(Message message) {
        return this.isValidParameter(message, HDOP)
                && this.isValidParameter(message, VDOP)
                && this.isValidParameter(message, PDOP);
    }

    private boolean isValidParameter(Message message, ParameterName name) {
        return compare(message.getParameter(name), this.property.getMaxValidDOP()) <= 0;
    }

    private static boolean isValidDateTime(Message message) {
        final Instant maxAllowableValidDateTime = now().plusSeconds(15);
        final Instant researchDateTime = message.getDatetime();
        return researchDateTime.isAfter(MIN_ALLOWABLE_VALID_DATE_TIME)
                && researchDateTime.isBefore(maxAllowableValidDateTime);
    }

    private static boolean isValidOrder(Message current, Message previous) {
        return current.getDatetime().isAfter(previous.getDatetime());
    }
}
