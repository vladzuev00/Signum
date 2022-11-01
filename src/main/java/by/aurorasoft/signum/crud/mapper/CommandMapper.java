package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.model.entity.DeviceEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public final class CommandMapper extends AbstractMapper<CommandEntity, Command> {
    private final EntityManager entityManager;

    public CommandMapper(ModelMapper modelMapper, EntityManager entityManager) {
        super(modelMapper, CommandEntity.class, Command.class);
        this.entityManager = entityManager;
    }

    @Override
    protected void mapSpecificFields(Command source, CommandEntity destination) {
        destination.setStatus(Command.Status.NOT_DEFINED);
        destination.setType(Command.Type.NOT_DEFINED);
        destination.setDevice(this.entityManager.getReference(DeviceEntity.class, source.getDeviceId()));
    }

    @Override
    protected Command createDto(CommandEntity entity) {
        return new Command(entity.getId(), entity.getText(), entity.getDevice().getId());
    }
}
