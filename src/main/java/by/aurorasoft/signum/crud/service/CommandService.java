package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.mapper.CommandMapper;
import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.model.entity.CommandEntity.Status;
import by.aurorasoft.signum.crud.model.entity.CommandEntity.Type;
import by.aurorasoft.signum.crud.repository.CommandRepository;
import by.nhorushko.crudgeneric.v2.service.AbstractCrudService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class CommandService extends AbstractCrudService<Long, CommandEntity, Command> {

    public CommandService(CommandMapper mapper, CommandRepository repository) {
        super(mapper, repository);
    }

    public Command save(Command command, Status status, Type type) {
        final CommandEntity entityToBeSaved = super.mapper.toEntity(command);
        entityToBeSaved.setStatus(status);
        entityToBeSaved.setType(type);
        final CommandEntity savedEntity = super.repository.save(entityToBeSaved);
        return super.mapper.toDto(savedEntity);
    }

    @Transactional
    public void updateByGivenStatus(Command command, Status newStatus) {
        final CommandRepository commandRepository = (CommandRepository) super.repository;
        commandRepository.updateByStatus(command.getId(), newStatus);
    }

    @Transactional
    public List<Command> findCommandsByStatuses(Status... statuses) {
        final CommandRepository commandRepository = (CommandRepository) super.repository;
        final List<CommandEntity> commandEntities = commandRepository.findByStatuses(Set.of(statuses));
        return super.mapper.toDto(commandEntities);
    }
}
