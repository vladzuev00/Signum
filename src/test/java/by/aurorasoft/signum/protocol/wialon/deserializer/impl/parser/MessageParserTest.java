package by.aurorasoft.signum.protocol.wialon.deserializer.impl.parser;

import by.aurorasoft.signum.ApplicationRunner;
import by.aurorasoft.signum.entity.GpsCoordinate;
import by.aurorasoft.signum.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationRunner.class})
public final class MessageParserTest {

    @Autowired
    private MessageParser messageParser;

    @Test
    public void messageShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";

        final Message actual = this.messageParser.parse(givenMessage);
        final Message expected = Message.builder()
                .dateTime(parse("2022-11-15T14:56:43Z"))
                .coordinate(new GpsCoordinate(57.406944F, 39.548332F))
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(177)
                .hdop(545.4554F)
                .parameters("param-name:654321,param-name:65.4321,param-name:param-value")
                .build();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void notValidMessageShouldNotBeParsed() {
        final String givenMessage = "not valid message";
        this.messageParser.parse(givenMessage);
    }
}
