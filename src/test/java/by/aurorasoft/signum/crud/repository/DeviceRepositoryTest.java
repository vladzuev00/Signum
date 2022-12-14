package by.aurorasoft.signum.crud.repository;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.aurorasoft.signum.crud.model.entity.UnitEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static by.aurorasoft.signum.crud.model.dto.Device.Type.TRACKER;
import static org.junit.Assert.*;

public final class DeviceRepositoryTest extends AbstractContextTest {

    @Autowired
    private DeviceRepository repository;

    @Test
    public void deviceShouldBeInserted() {
        final DeviceEntity givenDevice = DeviceEntity.builder()
                .imei("11111222223333344444")
                .phoneNumber("448447045")
                .type(TRACKER)
                .unit(super.entityManager.getReference(UnitEntity.class, 25551L))
                .build();
        super.startQueryCount();
        this.repository.save(givenDevice);
        super.checkQueryCount(1);
    }

    @Test
    public void deviceShouldBeFoundById() {
        super.startQueryCount();
        final DeviceEntity actual = this.repository.findById(25551L).orElseThrow();
        super.checkQueryCount(1);

        final DeviceEntity expected = DeviceEntity.builder()
                .id(25551L)
                .imei("355234055650192")
                .phoneNumber("+37257063997")
                .type(TRACKER)
                .unit(super.entityManager.getReference(UnitEntity.class, 25551L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void deviceShouldBeFoundByImei() {
        super.startQueryCount();
        final DeviceEntity actual = this.repository.findByImei("355234055650192").orElseThrow();
        super.checkQueryCount(1);

        final DeviceEntity expected = DeviceEntity.builder()
                .id(25551L)
                .imei("355234055650192")
                .phoneNumber("+37257063997")
                .type(TRACKER)
                .unit(super.entityManager.getReference(UnitEntity.class, 25551L))
                .build();

        checkEquals(expected, actual);
    }

    @Test
    public void deviceShouldNotBeFoundByImei() {
        super.startQueryCount();
        final Optional<DeviceEntity> optionalDevice = this.repository.findByImei("00000000000000000000");
        super.checkQueryCount(1);
        assertTrue(optionalDevice.isEmpty());
    }

    private static void checkEquals(DeviceEntity expected, DeviceEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getImei(), actual.getImei());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        assertSame(expected.getType(), actual.getType());
        assertEquals(expected.getUnit().getId(), actual.getUnit().getId());
    }
}
