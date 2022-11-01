package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.base.AbstractContextTest;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.aurorasoft.signum.crud.model.dto.Device.Type.TRACKER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class DeviceMapperTest extends AbstractContextTest {

    @Autowired
    private DeviceMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Device givenDto = new Device(255L, "11111222223333344444", "558447045", TRACKER);

        final DeviceEntity resultEntity = this.mapper.toEntity(givenDto);

        assertEquals(255, resultEntity.getId().longValue());
        assertEquals("11111222223333344444", resultEntity.getImei());
        assertEquals("558447045", resultEntity.getPhoneNumber());
        assertSame(TRACKER, resultEntity.getType());
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final DeviceEntity givenEntity = DeviceEntity.builder()
                .id(255L)
                .imei("11111222223333344444")
                .phoneNumber("558447045")
                .type(TRACKER)
                .build();

        final Device actual = this.mapper.toDto(givenEntity);
        final Device expected = new Device(255L, "11111222223333344444", "558447045", TRACKER);
        assertEquals(expected, actual);
    }
}
