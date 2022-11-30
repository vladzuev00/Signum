package by.aurorasoft.signum.protocol.core.service.receivemessage;

import by.aurorasoft.signum.config.property.MessageValidationProperty;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.ParameterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;
import static java.lang.Double.compare;
import static java.time.Instant.now;
import static java.time.Instant.parse;

@Component
@RequiredArgsConstructor
public class MessagePropertyValidator {
    private static final Instant MIN_ALLOWABLE_VALID_DATE_TIME = parse("2020-01-01T00:00:00Z");
    private static final int DELTA_SECONDS_FROM_NOW_MAX_ALLOWABLE_VALID_DATE_TIME = 15;

    private final MessageValidationProperty property;

    public boolean isValidAmountSatellite(Message message) {
        return message.getAmountSatellite() >= this.property.getMinValidAmountSatellite();
    }

    public boolean areValidCoordinateParameters(Message message) {
        final Map<ParameterName, Double> parameterNamesByValues = message.getParameterNamesByValues();
        return this.isContainCoordinateParameters(parameterNamesByValues)
                && this.isValidCoordinateParameter(parameterNamesByValues.get(HDOP))
                && this.isValidCoordinateParameter(parameterNamesByValues.get(VDOP))
                && this.isValidCoordinateParameter(parameterNamesByValues.get(PDOP));
    }

    public boolean isValidDateTime(Message message) {
        final Instant research = message.getDatetime();
        final Instant maxAllowableValidDateTime = now()
                .plusSeconds(DELTA_SECONDS_FROM_NOW_MAX_ALLOWABLE_VALID_DATE_TIME);
        return research.isAfter(MIN_ALLOWABLE_VALID_DATE_TIME)
                && research.isBefore(maxAllowableValidDateTime);
    }

    private boolean isContainCoordinateParameters(Map<ParameterName, Double> parameterNamesToValues) {
        return parameterNamesToValues.containsKey(HDOP)
                && parameterNamesToValues.containsKey(VDOP)
                && parameterNamesToValues.containsKey(PDOP);
    }

    private boolean isValidCoordinateParameter(Double research) {
        return compare(research, this.property.getMaxValidDOP()) <= 0;
    }
}
