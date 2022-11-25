package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser;

import by.aurorasoft.signum.crud.model.dto.Message;
import org.springframework.stereotype.Component;

@Component
public final class MessageParser {

    public Message parse(String source) {
        final MessageComponentsParser parser = new MessageComponentsParser(source);
        return Message.builder()
                .datetime(parser.parseDateTime())
                .coordinate(parser.parseCoordinate())
                .speed(parser.parseSpeed())
                .course(parser.parseCourse())
                .altitude(parser.parseAltitude())
                .amountSatellite(parser.parseAmountSatellite())
                .parameterNamesToValues(parser.parseParameters())
                .build();
    }
}
