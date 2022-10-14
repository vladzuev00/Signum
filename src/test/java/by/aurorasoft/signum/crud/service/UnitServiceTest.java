package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Tracker;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.model.dto.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class UnitServiceTest extends AbstractContextTest {

    @Autowired
    private UnitService service;

    @Test
    public void unitShouldBeFoundByTrackerImei() {
        final Unit actual = this.service.findByTrackerImei("355234055650192").orElseThrow();
        final Unit expected = new Unit(25551L, "unit_a",
                new User(25551L, "user_1"),
                new Tracker(25551L, "355234055650192", "+37257063997"));
        assertEquals(expected, actual);
    }

    @Test
    public void unitShouldNotBeFoundByTrackerImei() {
        final Optional<Unit> optionalUnit = this.service.findByTrackerImei("00000000000000000000");
        assertTrue(optionalUnit.isEmpty());
    }
}
