package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.dto.DeviceState;
import by.aurorasoft.signum.crud.model.dto.Message;
import by.aurorasoft.signum.crud.model.entity.DeviceStateEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class DeviceStateMapper extends AbsMapperEntityDto<DeviceStateEntity, DeviceState> {

    public DeviceStateMapper(ModelMapper modelMapper) {
        super(modelMapper, DeviceStateEntity.class, DeviceState.class);
    }

    @Override
    protected DeviceState create(DeviceStateEntity entity) {
        return new DeviceState(
                super.map(entity.getDevice(), Device.class),
                super.map(entity.getLastMessage(), Message.class)
        );
    }

    @Override
    protected void mapSpecificFields(DeviceState source, DeviceStateEntity destination) {
        destination.setId(source.getId());
    }
}
