package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.dto.Unit;
import by.aurorasoft.signum.crud.model.dto.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static by.aurorasoft.signum.crud.model.entity.DeviceEntity.Type.TRACKER;
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
                new Device(25551L, "355234055650192", "+37257063997", TRACKER));
        assertEquals(expected, actual);
    }

    @Test
    public void unitShouldNotBeFoundByTrackerImei() {
        final Optional<Unit> optionalUnit = this.service.findByTrackerImei("00000000000000000000");
        assertTrue(optionalUnit.isEmpty());
    }
}
