package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class TrackerMapperTest extends AbstractContextTest {

    @Autowired
    private TrackerMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
//        final Device givenTracker = new Device(255L, "11111222223333344444", "558447045");
//
//        final DeviceEntity actual = this.mapper.toEntity(givenTracker);
//        final DeviceEntity expected = new DeviceEntity(255L, "11111222223333344444", "558447045");
//        assertEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
//        final DeviceEntity givenTracker = new DeviceEntity(255L, "11111222223333344444",
//                "558447045");
//
//        final Device actual = this.mapper.toDto(givenTracker);
//        final Device expected = new Device(255L, "11111222223333344444", "558447045");
//        assertEquals(expected, actual);
    }
}
