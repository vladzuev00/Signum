package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser;

import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.ParameterName;
import org.springframework.stereotype.Component;

import java.util.Map;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.GSM_LEVEL;

@Component
public final class MessageParser {

    public Message parse(String source) {
        final MessageComponentsParser parser = new MessageComponentsParser(source);
        final Map<ParameterName, Double> parameters = parser.parseParameters();
        parameters.computeIfPresent(GSM_LEVEL,
                (parameterName, beforeConvertingValue) -> beforeConvertingValue * 100 / 6);
        return new Message(parser.parseDateTime(), parser.parseCoordinate(), parser.parseSpeed(),
                parser.parseCourse(), parser.parseAltitude(), parser.parseAmountSatellite(), parameters);
    }
}
