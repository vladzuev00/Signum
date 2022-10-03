package by.aurorasoft.signum.protocol.wialon.deserializer.impl;

import by.aurorasoft.signum.entity.GpsCoordinate;
import by.aurorasoft.signum.entity.Message;
import by.aurorasoft.signum.protocol.wialon.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.deserializer.impl.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.model.BlackBoxPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class BlackBoxPackageDeserializerTest {
    @Mock
    private MessageParser mockedParser;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    private PackageDeserializer deserializer;

    @Before
    public void initializeDeserializer() {
        this.deserializer = new BlackBoxPackageDeserializer(this.mockedParser);
    }

    @Test
    public void packageShouldBeDeserialized() {
        final String givenPackage = "#B#"
                + "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value"
                + "|"
                + "161122;145644;5544.6025;N;03739.6834;E;101;16;11;178;545.4555;18;19;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654322,param-name:2:66.4321,param-name:3:param-value";

        final List<Message> givenMessages = List.of(
                Message.builder()
                        .dateTime(parse("2022-11-15T14:56:43Z"))
                        .coordinate(new GpsCoordinate(57.406944F, 39.548332F))
                        .speed(100)
                        .course(15)
                        .altitude(10)
                        .amountSatellite(177)
                        .hdop(545.4554F)
                        .parameters("param-name:654321,param-name:65.4321,param-name:param-value")
                        .build(),
                Message.builder()
                        .dateTime(parse("2022-11-16T14:56:44Z"))
                        .coordinate(new GpsCoordinate(57.406944F, 39.548332F))
                        .speed(101)
                        .course(16)
                        .altitude(11)
                        .amountSatellite(178)
                        .hdop(545.4555F)
                        .parameters("param-name:654322,param-name:66.4321,param-name:param-value")
                        .build()
        );

        when(this.mockedParser.parse(anyString()))
                .thenReturn(givenMessages.get(0))
                .thenReturn(givenMessages.get(1));

        final Package actual = this.deserializer.deserialize(givenPackage);
        final BlackBoxPackage expected = new BlackBoxPackage(givenMessages);
        assertEquals(expected, actual);

        verify(this.mockedParser, times(2)).parse(this.stringArgumentCaptor.capture());
        final List<String> expectedCapturedStringArguments = List.of(
                "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                        + "5.5,4343.454544334,454.433,1;"
                        + "keydrivercode;"
                        + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value",
                "161122;145644;5544.6025;N;03739.6834;E;101;16;11;178;545.4555;18;19;"
                        + "5.5,4343.454544334,454.433,1;"
                        + "keydrivercode;"
                        + "param-name:1:654322,param-name:2:66.4321,param-name:3:param-value"
        );
        assertEquals(expectedCapturedStringArguments, this.stringArgumentCaptor.getAllValues());
    }
}