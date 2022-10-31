package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Device;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static by.aurorasoft.signum.crud.model.entity.DeviceEntity.Type.TRACKER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class DeviceServiceTest extends AbstractContextTest {

    @Autowired
    private DeviceService deviceService;

    @Test
    public void deviceShouldBeFoundByImei() {
        final Device actual = this.deviceService.findByImei("355234055650192").orElseThrow();
        final Device expected = new Device(25551L, "355234055650192", "+37257063997", TRACKER);
        assertEquals(expected, actual);
    }

    @Test
    public void deviceShouldNotBeFoundByImei() {
        final Optional<Device> optionalDevice = this.deviceService.findByImei("00000000000000000000");
        assertTrue(optionalDevice.isEmpty());
    }
}
