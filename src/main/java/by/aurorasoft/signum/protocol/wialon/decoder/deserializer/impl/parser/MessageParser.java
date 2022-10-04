package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser;

import by.aurorasoft.signum.entity.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class MessageParser {

    public MessageEntity parse(String source) {
        final MessageComponentsParser parser = new MessageComponentsParser(source);
        return MessageEntity.builder()
                .dateTime(parser.parseDateTime())
                .latitude(parser.parseLatitude())
                .longitude(parser.parseLongitude())
                .speed(parser.parseSpeed())
                .course(parser.parseCourse())
                .altitude(parser.parseAltitude())
                .amountSatellite(parser.parseAmountSatellite())
                .hdop(parser.parseHdop())
                .parameters(parser.parseParameters())
                .build();
    }
}
