package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.mapper.CommandMapper;
import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.repository.CommandRepository;
import by.nhorushko.crudgeneric.v2.service.AbstractCrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CommandService extends AbstractCrudService<Long, CommandEntity, Command> {

    public CommandService(CommandMapper mapper, CommandRepository repository) {
        super(mapper, repository);
    }

    public Command save(Command command, Command.Status status, Command.Type type) {
        final CommandEntity entityToBeSaved = super.mapper.toEntity(command);
        entityToBeSaved.setStatus(status);
        entityToBeSaved.setType(type);
        final CommandEntity savedEntity = super.repository.save(entityToBeSaved);
        return super.mapper.toDto(savedEntity);
    }

    public void updateStatus(Command command, Command.Status newStatus) {
        final CommandRepository commandRepository = (CommandRepository) super.repository;
        commandRepository.updateStatus(command.getId(), newStatus);
    }

    public List<Command> findCommandsByDeviceAndStatuses(Device device, Command.Status... statuses) {
        final CommandRepository commandRepository = (CommandRepository) super.repository;
        final List<CommandEntity> commandEntities = commandRepository
                .findByDeviceIdAndStatuses(device.getId(), Set.of(statuses));
        return super.mapper.toDto(commandEntities);
    }
}
