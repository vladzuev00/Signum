package by.aurorasoft.signum.protocol.core.service.receivemessage;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.dto.Message.GpsCoordinate;
import by.aurorasoft.signum.protocol.core.service.receivemessage.propertycalculator.impl.GPSOdometerCalculator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.aurorasoft.signum.crud.model.entity.MessageEntity.MessageType.VALID;
import static java.time.Instant.parse;
import static java.util.Collections.emptyMap;

public final class GPSOdometerCalculatorTest extends AbstractContextTest {

    @Autowired
    private GPSOdometerCalculator calculator;

    @Test
    public void gpsOdometerShouldBeCalculatedInCaseExistPreviousMessage() {
        final Message givenCurrent = Message.builder()
                .datetime(parse("2021-02-09T11:19:42Z"))
                .coordinate(new GpsCoordinate(53.91633F, 27.48064F))
                .speed(7)
                .course(8)
                .altitude(9)
                .amountSatellite(10)
                .parameterNamesByValues(emptyMap())
                .type(VALID)
                .build();
        final Message givenPrevious = Message.builder()
                .datetime(parse("2021-02-09T11:19:40Z"))
                .coordinate(new GpsCoordinate(53.91633F, 5.48064F))
                .speed(8)
                .course(9)
                .altitude(9)
                .amountSatellite(10)
                .parameterNamesByValues(emptyMap())
                .type(VALID)
                .build();

        this.calculator.calculate(givenCurrent, givenPrevious);

        final double actual = givenCurrent.getGpsOdometer();
        System.out.println(actual);
    }
}
