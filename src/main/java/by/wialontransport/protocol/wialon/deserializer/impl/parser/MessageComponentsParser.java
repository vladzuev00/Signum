package by.wialontransport.protocol.wialon.deserializer.impl.parser;

import by.wialontransport.entity.GpsCoordinate;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static java.time.LocalDateTime.parse;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.regex.Pattern.compile;

public final class MessageComponentsParser {
    private static final int GROUP_NUMBER_DATE_TIME = 1;

    private static final int GROUP_NUMBER_LATITUDE_DEGREES = 2;
    private static final int GROUP_NUMBER_LATITUDE_MINUTE = 3;
    private static final int GROUP_NUMBER_LATITUDE_MINUTE_SHARE = 4;
    private static final int GROUP_NUMBER_LATITUDE_TYPE = 5;
    private static final String ALIAS_SOUTH_LATITUDE_TYPE = "S";

    private static final int GROUP_NUMBER_LONGITUDE_DEGREES = 6;
    private static final int GROUP_NUMBER_LONGITUDE_MINUTE = 7;
    private static final int GROUP_NUMBER_LONGITUDE_MINUTE_SHARE = 8;
    private static final int GROUP_NUMBER_LONGITUDE_TYPE = 9;
    private static final String ALIAS_WESTERN_TYPE = "W";


    private static final int GROUP_NUMBER_SPEED = 10;
    private static final int GROUP_NUMBER_COURSE = 11;
    private static final int GROUP_NUMBER_ALTITUDE = 12;
    private static final int GROUP_NUMBER_AMOUNT_SATELLITE = 13;
    private static final int GROUP_NUMBER_AMOUNT_HDOP = 14;
    private static final int GROUP_NUMBER_PARAMETERS = 15;

    private static final DateTimeFormatter DATE_FORMATTER = ofPattern("ddMMyy;HHmmss");

    private static final String MESSAGE_REGEX
            = "((\\d{6}|(NA));(\\d{6}|(NA)));"         //date, time
            + "(\\d{2})(\\d{2})\\.(\\d+);([NS]);"      //latitude
            + "(\\d{3})(\\d{2})\\.(\\d+);([EW]);"      //longitude
            + "(\\d+|(NA));"                           //speed
            + "(\\d+|(NA));"                           //course
            + "(\\d+|(NA));"                           //altitude
            + "(\\d+|(NA));"                           //amountSatellite
            + "((\\d+\\.\\d+)|(NA));"                  //hdop
            + "(((\\d+|(NA));){2})"                    //inputs, outputs
            + "(((\\d+(\\.\\d+)?),?)*);"               //analogInputs
            + "(.*);"                                  //driverKeyCode
            + "((.+:[123]:.+,?)*)";                    //parameters
    private static final Pattern MESSAGE_PATTERN = compile(MESSAGE_REGEX);

    private final Matcher matcher;


    public MessageComponentsParser(String source) {
        this.matcher = MESSAGE_PATTERN.matcher(source);
    }

    public Instant parseDateTime() {
        final String timeString = this.matcher.group(GROUP_NUMBER_DATE_TIME);
        return parse(timeString, DATE_FORMATTER).toInstant(UTC);
    }

    public GpsCoordinate parseGpsCoordinate() {
        final float latitude = parseGpsCoordinate(GROUP_NUMBER_LATITUDE_DEGREES, GROUP_NUMBER_LATITUDE_MINUTE,
                GROUP_NUMBER_LATITUDE_MINUTE_SHARE, GROUP_NUMBER_LATITUDE_TYPE, ALIAS_SOUTH_LATITUDE_TYPE);
        final float longitude = parseGpsCoordinate(GROUP_NUMBER_LONGITUDE_DEGREES, GROUP_NUMBER_LONGITUDE_MINUTE,
                GROUP_NUMBER_LONGITUDE_MINUTE_SHARE, GROUP_NUMBER_LONGITUDE_TYPE, ALIAS_WESTERN_TYPE);
        return new GpsCoordinate(latitude, longitude);
    }

    public int parseSpeed() {
        return parseInt(this.matcher.group(GROUP_NUMBER_SPEED));
    }

    public int parseCourse() {
        return parseInt(this.matcher.group(GROUP_NUMBER_COURSE));
    }

    public int parseAltitude() {
        return parseInt(this.matcher.group(GROUP_NUMBER_ALTITUDE));
    }

    public int parseAmountSatellite() {
        return parseInt(this.matcher.group(GROUP_NUMBER_AMOUNT_SATELLITE));
    }

    public float parseHdop() {
        return parseFloat(this.matcher.group(GROUP_NUMBER_AMOUNT_HDOP));
    }

    /**
     * Parses parameters without type.
     * Example: count1:1:564,fuel:2:45.8,hw:3:V4.5 -> count1:564,fuel:45.8,hw:V4.5
     * @return parameters without type.
     */
    public String parseParameters() {
        final String parameters = this.matcher.group(GROUP_NUMBER_PARAMETERS);
        return parameters.replaceAll(":[^:]+", "");
    }

    private float parseGpsCoordinate(int groupNumberDegrees, int groupNumberMinute,
                                     int groupNumberMinuteShare, int groupNumberType,
                                     String aliasTypeToBeInverted) {
        final int degrees = parseInt(this.matcher.group(groupNumberDegrees));
        final int minutes = parseInt(this.matcher.group(groupNumberMinute));
        final int minuteShare = parseInt(this.matcher.group(groupNumberMinuteShare));
        final String type = this.matcher.group(groupNumberType);
        return (float) (signum(degrees)
                * (abs(degrees)
                + minutes / 60.0
                + minuteShare / 3600.0))
                * (type.equals(aliasTypeToBeInverted) ? -1 : 1);
    }
}
