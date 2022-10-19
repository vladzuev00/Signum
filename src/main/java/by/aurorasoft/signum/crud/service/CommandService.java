package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.mapper.CommandMapper;
import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.model.entity.CommandEntity.Status;
import by.aurorasoft.signum.crud.repository.CommandRepository;
import by.nhorushko.crudgeneric.v2.service.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public final class CommandService extends AbstractCrudService<Long, CommandEntity, Command> {

    public CommandService(CommandMapper mapper, CommandRepository repository) {
        super(mapper, repository);
    }

    public Command saveWithGivenStatus(Command command, Status status) {
        final CommandEntity entityToSave = super.mapper.toEntity(command);
        entityToSave.setStatus(status);
        final CommandEntity savedEntity = super.repository.save(entityToSave);
        return super.mapper.toDto(savedEntity);
    }

    public void updateByGivenStatus(Command command, Status newStatus) {
        final CommandEntity commandEntity = super.mapper.toEntity(command);
        commandEntity.setStatus(newStatus);
        super.repository.save(commandEntity);
    }
}
