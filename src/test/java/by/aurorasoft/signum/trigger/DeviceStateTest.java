package by.aurorasoft.signum.trigger;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.entity.DeviceStateEntity;
import by.aurorasoft.signum.crud.model.entity.MessageEntity;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

public final class DeviceStateTest extends AbstractContextTest {
    private static final String HQL_QUERY_TO_FIND_ALL_DEVICE_STATES = "SELECT e FROM DeviceStateEntity e";

    @Test
    public void deviceStatesShouldBeInserted() {
        final Set<DeviceStateEntity> actual = this.findAllDeviceStates();

        final Set<Long> actualIds = actual.stream()
                .map(DeviceStateEntity::getId)
                .collect(toSet());
        final Set<Long> expectedIds = Set.of(25551L, 25552L, 25553L, 25554L, 25555L);
        assertEquals(expectedIds, actualIds);

        assertTrue(actual.stream().allMatch(deviceState -> deviceState.getLastMessage() == null));
    }

    @Test
    @Sql(statements = "INSERT INTO message(id, time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type, gps_odometer, "
            + "ignition, engine_time, shock) "
            + "VALUES(255, '2000-02-18 04:05:06', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, "
            + "'VALID', 300, 1, 500, 2000)")
    public void deviceStatesShouldBeUpdatedAfterInsertingValidMessage() {
        final DeviceStateEntity actual = this.findDeviceStateById(25551L);
        final DeviceStateEntity expected = new DeviceStateEntity(25551L,
                super.entityManager.getReference(MessageEntity.class, 255L));
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getLastMessage().getId(), actual.getLastMessage().getId());
    }

    @Test
    @Sql(statements = "INSERT INTO message(id, time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type, gps_odometer, "
            + "ignition, engine_time, shock) "
            + "VALUES(255, '2000-02-18 04:05:06', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, "
            + "'INCORRECT', 300, 1, 500, 2000)")
    public void deviceStatesShouldNotBeUpdatedAfterInsertingIncorrectMessage() {
        final DeviceStateEntity actual = this.findDeviceStateById(25551L);
        assertNull(actual.getLastMessage());
    }

    private Set<DeviceStateEntity> findAllDeviceStates() {
        return super.entityManager
                .createQuery(HQL_QUERY_TO_FIND_ALL_DEVICE_STATES, DeviceStateEntity.class)
                .getResultStream()
                .collect(toSet());
    }

    @SuppressWarnings("all")
    private DeviceStateEntity findDeviceStateById(Long id) {
        return super.entityManager.find(DeviceStateEntity.class, id);
    }
}
