package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.MessageParser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;

public final class MessageParserTest extends AbstractContextTest {

    @Autowired
    private MessageParser messageParser;

    @Test
    public void messageShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";

        final Message actual = this.messageParser.parse(givenMessage);
        final Message expected = new Message(parse("2022-11-15T14:56:43Z"),
                new GpsCoordinate(57.406944F, 39.548332F), 100, 15, 10,
                177, 545.4554F,
                "param-name:654321,param-name:65.4321,param-name:param-value");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notValidMessageShouldNotBeParsed() {
        final String givenMessage = "not valid message";
        this.messageParser.parse(givenMessage);
    }
}
