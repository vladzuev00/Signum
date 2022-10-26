package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class CommandMapper extends AbstractMapper<CommandEntity, Command> {

    public CommandMapper(ModelMapper modelMapper) {
        super(modelMapper, CommandEntity.class, Command.class);
    }

    @Override
    protected void mapSpecificFields(Command source, CommandEntity destination) {
        destination.setStatus(CommandEntity.Status.NOT_DEFINED);
        destination.setType(CommandEntity.Type.NOT_DEFINED);
    }

    @Override
    protected Command createDto(CommandEntity entity) {
        final Device device = super.modelMapper.map(entity.getDevice(), Device.class);
        return new Command(entity.getId(), entity.getText(), device);
    }
}
