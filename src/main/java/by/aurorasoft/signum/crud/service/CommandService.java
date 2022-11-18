package by.aurorasoft.signum.crud.service;

import by.aurorasoft.signum.crud.mapper.CommandMapper;
import by.aurorasoft.signum.crud.model.dto.Command;
import by.aurorasoft.signum.crud.model.dto.Command.Status;
import by.aurorasoft.signum.crud.model.dto.Command.Type;
import by.aurorasoft.signum.crud.model.dto.Device;
import by.aurorasoft.signum.crud.model.entity.CommandEntity;
import by.aurorasoft.signum.crud.repository.CommandRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CommandService extends AbsServiceRUD<Long, CommandEntity, Command, CommandMapper, CommandRepository> {

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

    public void updateStatus(Command command, Status newStatus) {
        super.repository.updateStatus(command.getId(), newStatus);
    }

    public List<Command> findCommandsByDeviceAndStatuses(Device device, Status... statuses) {
        final List<CommandEntity> commandEntities = super.repository.findByDeviceIdAndStatuses(
                device.getId(), Set.of(statuses));
        return super.mapper.toDtos(commandEntities);
    }
}
