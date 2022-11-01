package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class DeviceMapper extends AbsMapperEntityDto<DeviceEntity, Device> {
    public DeviceMapper(ModelMapper modelMapper) {
        super(modelMapper, DeviceEntity.class, Device.class);
    }

    @Override
    protected Device create(DeviceEntity entity) {
        return new Device(entity.getId(), entity.getImei(), entity.getPhoneNumber(), entity.getType());
    }
}
