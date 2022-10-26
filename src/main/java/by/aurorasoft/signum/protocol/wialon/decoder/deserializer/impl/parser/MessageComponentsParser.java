package by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser;

import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.crud.model.dto.Message.ParameterName;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.exception.NotValidMessageException;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static by.aurorasoft.signum.crud.model.dto.Message.ParameterName.values;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static java.time.Instant.ofEpochSecond;
import static java.time.LocalDateTime.parse;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;
import static java.util.regex.Pattern.compile;

public final class MessageComponentsParser {
    private static final int GROUP_NUMBER_DATE_TIME = 1;

    private static final int GROUP_NUMBER_LATITUDE_DEGREES = 6;
    private static final int GROUP_NUMBER_LATITUDE_MINUTE = 7;
    private static final int GROUP_NUMBER_LATITUDE_MINUTE_SHARE = 8;
    private static final int GROUP_NUMBER_LATITUDE_TYPE = 9;
    private static final String ALIAS_SOUTH_LATITUDE_TYPE = "S";

    private static final int GROUP_NUMBER_LONGITUDE_DEGREES = 10;
    private static final int GROUP_NUMBER_LONGITUDE_MINUTE = 11;
    private static final int GROUP_NUMBER_LONGITUDE_MINUTE_SHARE = 12;
    private static final int GROUP_NUMBER_LONGITUDE_TYPE = 13;
    private static final String ALIAS_WESTERN_TYPE = "W";


    private static final int GROUP_NUMBER_SPEED = 14;
    private static final int GROUP_NUMBER_COURSE = 16;
    private static final int GROUP_NUMBER_ALTITUDE = 18;
    private static final int GROUP_NUMBER_AMOUNT_SATELLITE = 20;
    private static final int GROUP_NUMBER_AMOUNT_HDOP = 22;
    private static final int GROUP_NUMBER_PARAMETERS = 35;

    private static final DateTimeFormatter DATE_FORMATTER = ofPattern("ddMMyy;HHmmss");
    private static final String NOT_DEFINED_DATE_TIME_STRING = "NA;NA";
    private static final Instant NOT_DEFINED_DATE_TIME = ofEpochSecond(0);

    private static final String NOT_DEFINE_SPEED_STRING = "NA";
    private static final int NOT_DEFINED_SPEED = 0;

    private static final String NOT_DEFINED_COURSE_STRING = "NA";
    private static final int NOT_DEFINED_COURSE = 0;

    private static final String NOT_DEFINED_ALTITUDE_STRING = "NA";
    private static final int NOT_DEFINED_ALTITUDE = 0;

    private static final String NOT_DEFINED_HDOP_STRING = "NA";
    private static final float NOT_DEFINED_HDOP = 0.F;

    private static final String NOT_DEFINED_AMOUNT_SATELLITE_STRING = "NA";
    private static final int NOT_DEFINED_AMOUNT_SATELLITE = 0;
    private static final String DELIMITER_PARAMETERS = ",";
    private static final String DELIMITER_PARAMETER_COMPONENTS = ":";

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
            //NA comes from retranslator
            + "(((\\d+(\\.\\d+)?),?)*|(NA));"          //analogInputs
            + "(.*);"                                  //driverKeyCode
            + "(([^:]+:[123]:[^:]+,?)*)";              //parameters
    private static final Pattern MESSAGE_PATTERN = compile(MESSAGE_REGEX);

    private final Matcher matcher;


    public MessageComponentsParser(String source) {
        this.matcher = MESSAGE_PATTERN.matcher(source);
        if (!this.matcher.matches()) {
            throw new NotValidMessageException("Given message '" + source + "' isn't valid.");
        }
    }

    public Instant parseDateTime() {
        final String timeString = this.matcher.group(GROUP_NUMBER_DATE_TIME);
        return !timeString.equals(NOT_DEFINED_DATE_TIME_STRING)
                ? parse(timeString, DATE_FORMATTER).toInstant(UTC)
                : NOT_DEFINED_DATE_TIME;
    }

    public GpsCoordinate parseCoordinate() {
        final float latitude = this.parseGpsCoordinate(GROUP_NUMBER_LATITUDE_DEGREES, GROUP_NUMBER_LATITUDE_MINUTE,
                GROUP_NUMBER_LATITUDE_MINUTE_SHARE, GROUP_NUMBER_LATITUDE_TYPE, ALIAS_SOUTH_LATITUDE_TYPE);
        final float longitude = this.parseGpsCoordinate(GROUP_NUMBER_LONGITUDE_DEGREES, GROUP_NUMBER_LONGITUDE_MINUTE,
                GROUP_NUMBER_LONGITUDE_MINUTE_SHARE, GROUP_NUMBER_LONGITUDE_TYPE, ALIAS_WESTERN_TYPE);
        return new GpsCoordinate(latitude, longitude);
    }

    public int parseSpeed() {
        final String speedString = this.matcher.group(GROUP_NUMBER_SPEED);
        return !speedString.equals(NOT_DEFINE_SPEED_STRING) ? parseInt(speedString) : NOT_DEFINED_SPEED;
    }

    public int parseCourse() {
        final String courseString = this.matcher.group(GROUP_NUMBER_COURSE);
        return !courseString.equals(NOT_DEFINED_COURSE_STRING) ? parseInt(courseString) : NOT_DEFINED_COURSE;
    }

    public int parseAltitude() {
        final String altitudeString = this.matcher.group(GROUP_NUMBER_ALTITUDE);
        return !altitudeString.equals(NOT_DEFINED_ALTITUDE_STRING) ? parseInt(altitudeString) : NOT_DEFINED_ALTITUDE;
    }

    public int parseAmountSatellite() {
        final String amountSatelliteString = this.matcher.group(GROUP_NUMBER_AMOUNT_SATELLITE);
        return !amountSatelliteString.equals(NOT_DEFINED_AMOUNT_SATELLITE_STRING)
                ? parseInt(amountSatelliteString)
                : NOT_DEFINED_AMOUNT_SATELLITE;
    }

    public float parseHdop() {
        final String amountHdop = this.matcher.group(GROUP_NUMBER_AMOUNT_HDOP);
        return !amountHdop.equals(NOT_DEFINED_HDOP_STRING) ? parseFloat(amountHdop) : NOT_DEFINED_HDOP;
    }

    public Map<ParameterName, Float> parseParameters() {
        final String parameters = this.matcher.group(GROUP_NUMBER_PARAMETERS);
//        return stream(parameters.split(DELIMITER_PARAMETERS))
//                .filter(MessageComponentsParser::isReceivedParameter)
//                .map(parameter -> parameter.split(DELIMITER_PARAMETER_COMPONENTS))
//                .collect(toMap(
//                        components -> valueOf(components[0]),
//                        components -> parseDouble(components[2])));
        return null;
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

    private static boolean isReceivedParameter(String parameter) {
        return stream(values()).anyMatch(paramName -> parameter.startsWith(paramName.name()));
    }
}
