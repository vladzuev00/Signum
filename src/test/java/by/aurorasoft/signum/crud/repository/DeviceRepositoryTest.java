package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.aurorasoft.signum.crud.model.entity.DeviceEntity.Type.TRACKER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class DeviceRepositoryTest extends AbstractContextTest {
    @Autowired
    private DeviceRepository repository;

    @Test
    public void deviceShouldBeInserted() {
        final DeviceEntity givenDevice = DeviceEntity.builder()
                .imei("11111222223333344444")
                .phoneNumber("448447045")
                .type(TRACKER)
                .build();
        super.startQueryCount();
        this.repository.save(givenDevice);
        super.checkQueryCount(1);
    }

    @Test
    public void deviceShouldBeFoundById() {
        super.startQueryCount();
        final DeviceEntity foundDevice = this.repository.findById(25551L).orElseThrow();
        super.checkQueryCount(1);

        assertEquals(25551, foundDevice.getId().longValue());
        assertEquals("355234055650192", foundDevice.getImei());
        assertEquals("+37257063997", foundDevice.getPhoneNumber());
        assertSame(TRACKER, foundDevice.getType());
    }
}
