package by.aurorasoft.signum.protocol.wialon.deserializer.impl;

import by.aurorasoft.signum.entity.MessageEntity;
import by.aurorasoft.signum.entity.MessageEntity.GpsCoordinate;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.PackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.DataPackageDeserializer;
import by.aurorasoft.signum.protocol.wialon.decoder.deserializer.impl.parser.MessageParser;
import by.aurorasoft.signum.protocol.wialon.model.DataPackage;
import by.aurorasoft.signum.protocol.wialon.model.Package;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.time.Instant.parse;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class DataPackageDeserializerTest {

    @Mock
    private MessageParser mockedParser;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    private PackageDeserializer deserializer;

    @Before
    public void initializeDeserializer() {
        this.deserializer = new DataPackageDeserializer(this.mockedParser);
    }

    @Test
    public void packageShouldBeDeserialized() {
        final String givenPackage = "#D#151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        final MessageEntity givenMessage = MessageEntity.builder()
                .dateTime(parse("2022-11-15T14:56:43Z"))
                .coordinate(new GpsCoordinate(57.406944F, 39.548332F))
                .speed(100)
                .course(15)
                .altitude(10)
                .amountSatellite(177)
                .hdop(545.4554F)
                .parameters("param-name:654321,param-name:65.4321,param-name:param-value")
                .build();
        when(this.mockedParser.parse(anyString())).thenReturn(givenMessage);

        final Package actual = this.deserializer.deserialize(givenPackage);
        final DataPackage expected = new DataPackage(singletonList(givenMessage));
        assertEquals(expected, actual);

        verify(this.mockedParser, times(1)).parse(this.stringArgumentCaptor.capture());
        final String expectedCapturedSerializedMessage = "151122;145643;5544.6025;N;03739.6834;E;100;15;10;177;"
                + "545.4554;17;18;"
                + "5.5,4343.454544334,454.433,1;"
                + "keydrivercode;"
                + "param-name:1:654321,param-name:2:65.4321,param-name:3:param-value";
        assertEquals(expectedCapturedSerializedMessage, this.stringArgumentCaptor.getValue());
    }
}
