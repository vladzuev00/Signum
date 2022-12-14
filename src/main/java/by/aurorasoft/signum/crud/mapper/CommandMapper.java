package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Command.Status;
import by.aurorasoft.signum.crud.model.dto.Command.Type;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public final class CommandMapper extends AbsMapperEntityDto<CommandEntity, Command> {
    private static final Long NOT_DEFINED_DEVICE_ID = 0L;

    private final EntityManager entityManager;

    public CommandMapper(ModelMapper modelMapper, EntityManager entityManager) {
        super(modelMapper, CommandEntity.class, Command.class);
        this.entityManager = entityManager;
    }

    @Override
    protected void mapSpecificFields(Command source, CommandEntity destination) {
        destination.setStatus(Status.NOT_DEFINED);
        destination.setType(Type.NOT_DEFINED);
        destination.setDevice(this.entityManager.getReference(DeviceEntity.class, source.getDeviceId()));
    }

    @Override
    protected Command create(CommandEntity entity) {
        return new Command(entity.getId(), entity.getText(), mapDeviceId(entity));
    }

    private static Long mapDeviceId(CommandEntity entity) {
        return entity.getDevice() != null ? entity.getDevice().getId() : NOT_DEFINED_DEVICE_ID;
    }
}
