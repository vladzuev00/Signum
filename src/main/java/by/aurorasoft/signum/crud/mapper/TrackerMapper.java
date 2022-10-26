package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class TrackerMapper extends AbstractMapper<DeviceEntity, Device> {
    public TrackerMapper(ModelMapper modelMapper) {
        super(modelMapper, DeviceEntity.class, Device.class);
    }

    @Override
    protected Device createDto(DeviceEntity entity) {
        return new Device(entity.getId(), entity.getImei(), entity.getPhoneNumber(), entity.getType());
    }
}
