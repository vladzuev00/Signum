package by.aurorasoft.signum.protocol.wialon.deserializer.impl.parser;

import by.aurorasoft.signum.dto.MessageDto.GpsCoordinate;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.MessageComponentsParser;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.Instant;

import static java.time.Instant.ofEpochSecond;
import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class MessageComponentsParserTest {
    private static final String FIELD_NAME_MESSAGE_REGEX = "MESSAGE_REGEX";

    private final String messageRegex;

    public MessageComponentsParserTest()
            throws Exception {
        this.messageRegex = findMessageRegex();
    }

    @Test
    public void parserShouldBeCreated() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        new MessageComponentsParser(givenMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parserShouldNotBeCreatedWithNotValidMessage() {
        final String givenMessage = "not valid message";
        new MessageComponentsParser(givenMessage);
    }

    @Test
    public void messageShouldMatchToRegex() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        assertTrue(givenMessage.matches(this.messageRegex));
    }

    @Test
    public void messageWithNotDefinedComponentsShouldMatchToRegex() {
        final String givenMessage = "NA;NA;5544.6025;N;03739.6834;E;NA;NA;NA;NA;NA;NA;NA;;NA;";
        assertTrue(givenMessage.matches(this.messageRegex));
    }

    @Test
    public void dateTimeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final Instant actual = parser.parseDateTime();
        final Instant expected = parse("2022-11-15T14:56:43Z");
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedDateTimeShouldBeParsed() {
        final String givenMessage = "NA;NA;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final Instant actual = parser.parseDateTime();
        final Instant expected = ofEpochSecond(0);
        assertEquals(expected, actual);
    }

    @Test
    public void coordinateShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final GpsCoordinate actual = parser.parseCoordinate();
        final GpsCoordinate expected = new GpsCoordinate(57.406944F, 39.548332F);
        assertEquals(expected, actual);
    }

    @Test
    public void speedShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseSpeed();
        final int expected = 100;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedSpeedShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;NA;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseSpeed();
        final int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    public void courseShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseCourse();
        final int expected = 15;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedCourseShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;NA;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseCourse();
        final int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    public void altitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseAltitude();
        final int expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedAltitudeShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;NA;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseAltitude();
        final int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    public void amountSatelliteShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseAmountSatellite();
        final int expected = 177;
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedAmountSatelliteShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;NA;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final int actual = parser.parseAmountSatellite();
        final int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    public void hdopShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final float actual = parser.parseHdop();
        final float expected = 545.4554F;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void notDefinedHdopShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;NA;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final float actual = parser.parseHdop();
        final float expected = 0.F;
        assertEquals(expected, actual, 0.);
    }

    @Test
    public void parametersShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final String actual = parser.parseParameters();
        final String expected = "param-name:654321,param-name:65.4321,param-name:param-value";
        assertEquals(expected, actual);
    }

    @Test
    public void notDefinedParametersShouldBeParsed() {
        final String givenMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;";
        final MessageComponentsParser parser = new MessageComponentsParser(givenMessage);

        final String actual = parser.parseParameters();
        final String expected = "";
        assertEquals(expected, actual);
    }

    private static String findMessageRegex()
            throws Exception {
        final Field messageRegexField = MessageComponentsParser.class.getDeclaredField(FIELD_NAME_MESSAGE_REGEX);
        messageRegexField.setAccessible(true);
        try {
            return (String) messageRegexField.get(null);
        } finally {
            messageRegexField.setAccessible(false);
        }
    }
}
