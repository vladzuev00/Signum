package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser;

import by.aurorasoft.signum.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class MessageParser {

    public MessageDto parse(String source) {
        final MessageComponentsParser parser = new MessageComponentsParser(source);
        return new MessageDto(parser.parseDateTime(), parser.parseCoordinate(), parser.parseSpeed(), parser.parseCourse(),
                parser.parseAltitude(), parser.parseAmountSatellite(), parser.parseHdop(), parser.parseParameters());
    }
}
