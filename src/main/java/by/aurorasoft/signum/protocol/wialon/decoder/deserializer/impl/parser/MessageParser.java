package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser;

import by.aurorasoft.signum.crud.model.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class MessageParser {

    public Message parse(String source) {
        final MessageComponentsParser parser = new MessageComponentsParser(source);
        return new Message(parser.parseDateTime(), parser.parseCoordinate(), parser.parseSpeed(),
                parser.parseCourse(), parser.parseAltitude(), parser.parseAmountSatellite(), parser.parseHdop(),
                parser.parseParameters());
    }
}
