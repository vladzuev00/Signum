package by.aurorasoft.signum.trigger;

import by.aurorasoft.signum.base.AbstractContextTest;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import static java.util.Set.copyOf;
import static org.junit.Assert.assertEquals;

public final class DeviceStateTest extends AbstractContextTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Test
    public void deviceStatesShouldBeInserted() {
        final Set<DeviceState> actual = copyOf(this.jdbcTemplate.query(
                "SELECT device_id, last_message_id, last_valid_latitude, last_valid_longitude "
                        + "FROM device_state",
                new DeviceStateRowMapper()));
        final Set<DeviceState> expected = Set.of(
                new DeviceState(25551, null, null, null),
                new DeviceState(25552, null, null, null),
                new DeviceState(25553, null, null, null),
                new DeviceState(25554, null, null, null),
                new DeviceState(25555, null, null, null)
        );
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO message(id, time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type) "
            + "VALUES(255, '2000-02-18 04:05:06', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, "
            + "'VALID')")
    public void deviceStatesShouldBeUpdatedAfterInsertingValidMessage() {
        final DeviceState actual = this.findDeviceState(25551);
        final DeviceState expected = new DeviceState(25551, 255L, 4.4F,
                5.5F);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO message(id, time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type) "
            + "VALUES(255, '2000-02-18 04:05:06', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, "
            + "'CORRECT')")
    public void deviceStatesShouldBeUpdatedAfterInsertingCorrectMessage() {
        final DeviceState actual = this.findDeviceState(25551);
        final DeviceState expected = new DeviceState(25551, 255L, null,
                null);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO message(id, time, latitude, longitude, speed, course, altitude, amount_satellite, "
            + "device_id, gsm_level, onboard_voltage, eco_cornering, eco_acceleration, eco_braking, type) "
            + "VALUES(255, '2000-02-18 04:05:06', 4.4, 5.5, 10, 11, 12, 13, 25551, 44, 1500, 1600, 1700, 1800, "
            + "'INCORRECT')")
    public void deviceStatesShouldNotBeUpdatedAfterInsertingIncorrectMessage() {
        final DeviceState actual = this.findDeviceState(25551);
        final DeviceState expected = new DeviceState(25551, null, null,
                null);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("all")
    private DeviceState findDeviceState(final long deviceId) {
        return this.jdbcTemplate.queryForObject(
                "SELECT device_id, last_message_id, last_valid_latitude, last_valid_longitude "
                        + "FROM device_state "
                        + "WHERE device_id = :deviceId",
                Map.of("deviceId", deviceId),
                new DeviceStateRowMapper()
        );
    }

    @Value
    private static class DeviceState {
        long deviceId;
        Long lastMessageId;
        Float lastValidLatitude;
        Float lastValidLongitude;
    }

    private static final class DeviceStateRowMapper implements RowMapper<DeviceState> {
        private static final String COLUMN_NAME_DEVICE_ID = "device_id";
        private static final String COLUMN_NAME_LAST_MESSAGE_ID = "last_message_id";
        private static final String COLUMN_NAME_LAST_VALID_LATITUDE = "last_valid_latitude";
        private static final String COLUMN_NAME_LAST_VALID_LONGITUDE = "last_valid_longitude";

        @Override
        public DeviceState mapRow(@NotNull ResultSet resultSet, int rowNumber)
                throws SQLException {
            final long deviceId = resultSet.getLong(COLUMN_NAME_DEVICE_ID);

            Long lastMessageId = resultSet.getLong(COLUMN_NAME_LAST_MESSAGE_ID);
            if (resultSet.wasNull()) {
                lastMessageId = null;
            }

            Float lastValidLatitude = resultSet.getFloat(COLUMN_NAME_LAST_VALID_LATITUDE);
            if (resultSet.wasNull()) {
                lastValidLatitude = null;
            }

            Float lastValidLongitude = resultSet.getFloat(COLUMN_NAME_LAST_VALID_LONGITUDE);
            if (resultSet.wasNull()) {
                lastValidLongitude = null;
            }

            return new DeviceState(deviceId, lastMessageId, lastValidLatitude, lastValidLongitude);
        }
    }
}
