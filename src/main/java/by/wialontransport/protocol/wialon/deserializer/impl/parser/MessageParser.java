package by.wialontransport.protocol.wialon.deserializer.impl.parser;

import by.wialontransport.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class MessageParser {

    public Message parse(String source) {
        final MessageComponentsParser parser = new MessageComponentsParser(source);
        return Message.builder()
                .dateTime(parser.parseDateTime())
                .coordinate(parser.parseGpsCoordinate())
                .speed(parser.parseSpeed())
                .course(parser.parseCourse())
                .altitude(parser.parseAltitude())
                .amountSatellite(parser.parseAmountSatellite())
                .hdop(parser.parseHdop())
                .parameters(parser.parseParameters())
                .build();
    }
}
