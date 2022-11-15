package by.aurorasoft.signum.crud.service.message;

import by.aurorasoft.signum.config.property.MessageValidationProperty;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.ParameterName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.*;
import static java.lang.Double.compare;
import static java.time.Instant.now;
import static java.time.Instant.parse;

@Component
@RequiredArgsConstructor
class MessagePropertyValidator {
    private static final Instant MIN_ALLOWABLE_VALID_DATE_TIME = parse("2020-01-01T00:00:00Z");
    private static final int DELTA_SECONDS_FROM_NOW_MAX_ALLOWABLE_VALID_DATE_TIME = 15;

    private final MessageValidationProperty property;

    public boolean isValidAmountSatellite(Message research) {
        return research.getAmountSatellite() >= this.property.getMinValidAmountSatellite();
    }

    public boolean areValidCoordinates(Message research) {
        return this.isValidParameter(research, HDOP)
                && this.isValidParameter(research, VDOP)
                && this.isValidParameter(research, PDOP);
    }

    public boolean isValidDateTime(Message research) {
        final Instant maxAllowableValidDateTime = now()
                .plusSeconds(DELTA_SECONDS_FROM_NOW_MAX_ALLOWABLE_VALID_DATE_TIME);
        final Instant researchDateTime = research.getDatetime();
        return researchDateTime.isAfter(MIN_ALLOWABLE_VALID_DATE_TIME)
                && researchDateTime.isBefore(maxAllowableValidDateTime);
    }

    private boolean isValidParameter(Message research, ParameterName name) {
        return compare(research.getParameter(name), this.property.getMaxValidDOP()) <= 0;
    }
}
