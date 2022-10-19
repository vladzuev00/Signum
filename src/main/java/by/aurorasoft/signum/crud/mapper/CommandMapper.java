package by.aurorasoft.signum.crud.mapper;

import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Tracker;
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
    protected Command createDto(CommandEntity entity) {
        final Tracker tracker = super.modelMapper.map(entity.getTracker(), Tracker.class);
        return new Command(entity.getId(), entity.getText(), tracker);
    }
}
